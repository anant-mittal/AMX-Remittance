package com.amx.jax.rbaac.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.rbaac.dao.RbaacDao;
import com.amx.jax.rbaac.dbmodel.FSEmployee;
import com.amx.jax.rbaac.dbmodel.Permission;
import com.amx.jax.rbaac.dbmodel.Role;
import com.amx.jax.rbaac.dbmodel.UserRoleMapping;
import com.amx.jax.rbaac.dto.request.RoleRequestDTO;
import com.amx.jax.rbaac.dto.request.UserRoleMappingsRequestDTO;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.response.PermissionResposeDTO;
import com.amx.jax.rbaac.dto.response.RoleMappingForEmployee;
import com.amx.jax.rbaac.dto.response.RoleResponseDTO;
import com.amx.jax.rbaac.dto.response.UserRoleMappingDTO;
import com.amx.jax.rbaac.dto.response.UserRoleMappingsResponseDTO;
import com.amx.jax.rbaac.error.RbaacServiceError;
import com.amx.jax.rbaac.exception.AuthServiceException;
import com.amx.jax.util.ObjectConverter;
import com.amx.utils.JsonUtil;

/**
 * The Class UserRoleService.
 */
@Service
public class UserRoleService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerService.getLogger(UserRoleService.class);

	/** The login dao. */
	@Autowired
	RbaacDao rbaacDao;

	/**
	 * Gets the all permissions.
	 *
	 * @param ipAddr
	 *            the ip addr
	 * @param deviceId
	 *            the device id
	 * @return the all permissions
	 */
	public List<PermissionResposeDTO> getAllPermissions(String ipAddr, String deviceId) {

		LOGGER.info("Get All Perms API Called");

		List<Permission> permissionList = rbaacDao.getAllPermissions();

		List<PermissionResposeDTO> permDTOList = new ArrayList<PermissionResposeDTO>();

		for (Permission permission : permissionList) {
			PermissionResposeDTO permissionResposeDTO = new PermissionResposeDTO();

			permissionResposeDTO.setId(permission.getId());
			permissionResposeDTO.setPermission(permission.getPermission());
			permissionResposeDTO.setContext(permission.getContext());
			permissionResposeDTO.setFlags(permission.getFlags());
			permissionResposeDTO.setInfo(permission.getInfo());

			try {
				permissionResposeDTO.setAccessList(JsonUtil.getListFromJsonString(permission.getAccessListJson()));
				permissionResposeDTO.setScopeList(JsonUtil.getListFromJsonString(permission.getScopeListJson()));
			} catch (IOException e) {

				throw new AuthServiceException("Incompatible JSON Data Type", RbaacServiceError.INCOMPATIBLE_DATA_TYPE,
						e);

			}

			permDTOList.add(permissionResposeDTO);

		}

		return permDTOList;
	}

	/**
	 * Gets the all roles.
	 *
	 * @param ipAddr
	 *            the ip addr
	 * @param deviceId
	 *            the device id
	 * @return the all roles
	 */
	@SuppressWarnings("unchecked")
	public List<RoleResponseDTO> getAllRoles(String ipAddr, String deviceId) {

		LOGGER.info("Get All Roles API Called");

		List<Role> roleList = rbaacDao.getAllRoles();

		List<RoleResponseDTO> rolesResponseDTOList = new ArrayList<RoleResponseDTO>();

		for (Role role : roleList) {
			RoleResponseDTO roleResponseDTO = new RoleResponseDTO();

			roleResponseDTO.setId(role.getId());
			roleResponseDTO.setRole(role.getRole());
			roleResponseDTO.setSuspended(role.getSuspended());
			roleResponseDTO.setFlags(role.getFlags());
			roleResponseDTO.setInfo(role.getInfo());
			roleResponseDTO.setCreatedDate(role.getCreatedDate());
			roleResponseDTO.setUpdatedDate(role.getUpdatedDate());
			roleResponseDTO.setPermissionMap(JsonUtil.fromJson(role.getPermissionsJson(), Map.class));

			rolesResponseDTOList.add(roleResponseDTO);
		}

		return rolesResponseDTOList;

	}

	/**
	 * Save role.
	 *
	 * @param roleRequestDTO
	 *            the role request DTO
	 * @return the role response DTO
	 */
	public RoleResponseDTO saveRole(RoleRequestDTO roleRequestDTO) {

		synchronized (this) {

			Role role;
			Date today = new Date();

			if (StringUtils.isEmpty(roleRequestDTO.getRole())) {
				throw new AuthServiceException(RbaacServiceError.INVALID_ROLE, "Role Can Not be null or Empty");
			}

			/**
			 * Case New Role is Defined.
			 */
			if (roleRequestDTO.getId() == null || roleRequestDTO.getId().intValue() == 0) {

				/**
				 * Check for Duplicate Role
				 */
				List<Role> roleList = rbaacDao.getAllRoles();
				for (Role existingRole : roleList) {
					if (existingRole.getRole().trim().equalsIgnoreCase(roleRequestDTO.getRole().trim())) {
						throw new AuthServiceException(RbaacServiceError.DUPLICATE_ROLE, "Duplicate Role");
					}
				}

				role = new Role();
				role.setId(new BigDecimal(0));
				role.setCreatedDate(today);

			} else { // Old Role is Updated

				role = rbaacDao.getRoleById(roleRequestDTO.getId());

				if (role == null) {
					throw new AuthServiceException(RbaacServiceError.INVALID_ROLE_DEFINITION,
							"Invalid Role: No Role Exists with Given Id");
				}
			}

			/**
			 * Validate Permissions
			 */
			validatePerms(roleRequestDTO);

			/**
			 * Fill In Role Updated Role Data
			 */
			role.setRole(roleRequestDTO.getRole());
			role.setPermissionsJson(JsonUtil.toJson(roleRequestDTO.getPermissionMap()));
			role.setSuspended("N");
			role.setFlags(new BigDecimal(0));
			role.setInfo("{}");
			role.setUpdatedDate(today);

			rbaacDao.saveRole(role);

			return ObjectConverter.convertRoleToRoleResponseDTO(role);

		}
	}

	/**
	 * Gets the user role mappings for branch.
	 *
	 * @param countryBranchId
	 *            the country branch id
	 * @param ipAddr
	 *            the ip addr
	 * @param deviceId
	 *            the device id
	 * @return the user role mappings for branch
	 */
	public UserRoleMappingsResponseDTO getUserRoleMappingsForBranch(BigDecimal countryBranchId, String ipAddr,
			String deviceId) {

		UserRoleMappingsResponseDTO urmResponseDTO = new UserRoleMappingsResponseDTO();

		List<FSEmployee> employeeList = rbaacDao.getEmployeesByCountryBranchId(countryBranchId);

		if (employeeList == null || employeeList.isEmpty()) {
			return urmResponseDTO;
		}

		Map<BigDecimal, EmployeeDetailsDTO> employeeInfoMap = new HashMap<BigDecimal, EmployeeDetailsDTO>();

		List<BigDecimal> empIdList = new ArrayList<BigDecimal>();

		for (FSEmployee employee : employeeList) {
			empIdList.add(employee.getEmployeeId());

			/**
			 * Populate the Employee Details.
			 */
			employeeInfoMap.put(employee.getEmployeeId(), ObjectConverter.convertEmployeeToEmpDetailsDTO(employee));

		}

		urmResponseDTO.setEmployeeInfoMap(employeeInfoMap);

		List<Role> roleList = rbaacDao.getAllRoles();

		Map<BigDecimal, RoleResponseDTO> roleInfoMap = new HashMap<BigDecimal, RoleResponseDTO>();

		for (Role role : roleList) {
			roleInfoMap.put(role.getId(), ObjectConverter.convertRoleToRoleResponseDTO(role));
		}

		urmResponseDTO.setRoleInfoMap(roleInfoMap);

		List<UserRoleMapping> userRoleMappingList = rbaacDao.getUserRoleMappingsByEmployeeIds(empIdList);

		Map<BigDecimal, UserRoleMappingDTO> userRoleMappingInfoMap = new HashMap<BigDecimal, UserRoleMappingDTO>();

		for (UserRoleMapping userRoleMapping : userRoleMappingList) {
			userRoleMappingInfoMap.put(userRoleMapping.getEmployeeId(),
					ObjectConverter.convertUrmToUrmDTO(userRoleMapping));
		}

		urmResponseDTO.setUserRoleMappingInfoMap(userRoleMappingInfoMap);

		return urmResponseDTO;
	}

	/**
	 * Update user role mappings.
	 *
	 * @param urmRequestDTO
	 *            the urm request DTO
	 * @return the list
	 */
	public List<UserRoleMappingDTO> updateUserRoleMappings(UserRoleMappingsRequestDTO urmRequestDTO) {

		List<UserRoleMappingDTO> urmInfoList = urmRequestDTO.getUserRoleMappingInfoList();

		List<UserRoleMapping> persistURMappings = new ArrayList<UserRoleMapping>();
		List<UserRoleMapping> deleteURMappings = new ArrayList<UserRoleMapping>();

		if (urmInfoList == null || urmInfoList.isEmpty()) {
			return new ArrayList<UserRoleMappingDTO>();
		}

		for (UserRoleMappingDTO userRoleMappingDTO : urmInfoList) {
			if (userRoleMappingDTO.getId() == null || userRoleMappingDTO.getId().longValue() == 0) {

				// case 1: New Mapping Created.

				UserRoleMapping urm = ObjectConverter.convertUrmDTOToUserRoleMapping(userRoleMappingDTO);

				if (!validateUserRoleMapping(userRoleMappingDTO)) {

					throw new AuthServiceException(RbaacServiceError.INVALID_USER_ROLE_MAPPINGS,
							"Invalid User Role Mappings: One or more role mappings are Present for User Role Mapping Id: "
									+ userRoleMappingDTO.getId() + ", EmployeeId: " + userRoleMappingDTO.getEmployeeId()
									+ ", RoleId: " + userRoleMappingDTO.getRoleId());
				}

				persistURMappings.add(urm);

			} else if (null != userRoleMappingDTO.getIsDeleted() && userRoleMappingDTO.getIsDeleted()) {

				// case 2: Mapping is to be deleted.

				UserRoleMapping existingMapping = rbaacDao.getUserRoleMappingById(userRoleMappingDTO.getId());

				if (existingMapping == null) {

					LOGGER.error("Invalid Delete Request for User Role Mapping Id: " + userRoleMappingDTO.getId()
							+ ", EmployeeId: " + userRoleMappingDTO.getEmployeeId() + ", RoleId: "
							+ userRoleMappingDTO.getRoleId());
					continue;

				} else if (!validateUserRoleMapping(userRoleMappingDTO)) {

					throw new AuthServiceException(RbaacServiceError.INVALID_USER_ROLE_MAPPINGS,
							"Invalid User Role Mappings: One or more role mappings are Invalid for User Role Mapping Id: "
									+ userRoleMappingDTO.getId() + ", EmployeeId: " + userRoleMappingDTO.getEmployeeId()
									+ ", RoleId: " + userRoleMappingDTO.getRoleId());
				}

				deleteURMappings.add(existingMapping);

			} else {

				// case 3: Mapping is to be Updated.

				UserRoleMapping existingMapping = rbaacDao.getUserRoleMappingById(userRoleMappingDTO.getId());

				if (existingMapping == null) {

					LOGGER.error("Invalid Delete Request for User Role Mapping Id: " + userRoleMappingDTO.getId()
							+ ", EmployeeId: " + userRoleMappingDTO.getEmployeeId() + ", RoleId: "
							+ userRoleMappingDTO.getRoleId());
					continue;

				} else if (!validateUserRoleMapping(userRoleMappingDTO)) {

					throw new AuthServiceException(RbaacServiceError.INVALID_USER_ROLE_MAPPINGS,
							"Invalid User Role Mappings: One or more role mappings are Invalid for User Role Mapping Id: "
									+ userRoleMappingDTO.getId() + ", EmployeeId: " + userRoleMappingDTO.getEmployeeId()
									+ ", RoleId: " + userRoleMappingDTO.getRoleId());

				} else if (userRoleMappingDTO.getEmployeeId().longValue() != existingMapping.getEmployeeId().longValue()) {

					throw new AuthServiceException(RbaacServiceError.ILLEGAL_USER_ROLE_MAPPING_MODIFICATION,
							"Illegal User Role Mappings Modification: One or more role mapping modifications are Invalid for User Role Mapping Id: "
									+ userRoleMappingDTO.getId() + ", EmployeeId: " + userRoleMappingDTO.getEmployeeId()
									+ ", RoleId: " + userRoleMappingDTO.getRoleId());
				}

				existingMapping.setRoleId(userRoleMappingDTO.getRoleId());
				existingMapping.setUpdatedDate(new Date());

				persistURMappings.add(existingMapping);
			}
		}

		List<UserRoleMappingDTO> respUrmDtoList = new ArrayList<UserRoleMappingDTO>();

		// Insert / Update entries to be persisted.
		if (!persistURMappings.isEmpty()) {

			List<UserRoleMapping> savedUrm = rbaacDao.saveUserRoleMappings(persistURMappings);

			for (UserRoleMapping urm : savedUrm) {
				respUrmDtoList.add(ObjectConverter.convertUrmToUrmDTO(urm));
			}
		}

		if (!deleteURMappings.isEmpty()) {

			rbaacDao.deleteUserRoleMappings(deleteURMappings);

			for (UserRoleMapping urm : deleteURMappings) {
				UserRoleMappingDTO urmDto = ObjectConverter.convertUrmToUrmDTO(urm);
				urmDto.setIsDeleted(Boolean.TRUE);

				respUrmDtoList.add(urmDto);
			}
		}

		return respUrmDtoList;
	}

	/**
	 * Validate perms.
	 *
	 * @param roleRequestDTO
	 *            the role request DTO
	 * @return true, if successful
	 */
	private boolean validatePerms(RoleRequestDTO roleRequestDTO) {

		List<Permission> permList = rbaacDao.getAllPermissions();

		Map<String, Permission> keyPermMap = new HashMap<String, Permission>();
		for (Permission permission : permList) {
			keyPermMap.put(permission.getPermission().trim(), permission);
		}

		for (Entry<String, Map<String, String>> entry : roleRequestDTO.getPermissionMap().entrySet()) {
			if (keyPermMap.containsKey(entry.getKey())) {

				Permission permission = keyPermMap.get(entry.getKey());

				String accessTypeJson = permission.getAccessListJson();
				String scopeJson = permission.getScopeListJson();

				for (Entry<String, String> innerEntry : entry.getValue().entrySet()) {
					if (!accessTypeJson.contains(innerEntry.getKey()) || !scopeJson.contains(innerEntry.getValue())) {
						throw new AuthServiceException(RbaacServiceError.INVALID_ACCESS_TYPE_SCOPE,
								"Invalid Permission: One or more permissions are Invalid: " + entry.getKey() + " : "
										+ innerEntry.getKey() + " : " + innerEntry.getValue());
					}
				}

			} else {
				throw new AuthServiceException(RbaacServiceError.INVALID_PERMISSION,
						"Invalid Permission: One or more permissions are Invalid: " + entry.getKey());
			}

		} // for

		return true;

	}

	/**
	 * Validates User role Mappings.
	 *
	 * @param urm
	 *            the urm
	 * @return true, if successful
	 */
	private boolean validateUserRoleMapping(UserRoleMappingDTO urm) {

		FSEmployee employee = rbaacDao.getEmployeeByEmployeeId(urm.getEmployeeId());
		Role role = rbaacDao.getRoleById(urm.getRoleId());

		if (employee == null || role == null) {
			return false;
		}

		if (null != urm.getId() && urm.getId().longValue() == 0l) {
			// In Case Requested for New user Role Mapping and Mapping already exists
			if (null != rbaacDao.getUserRoleMappingByEmployeeId(urm.getEmployeeId())) {
				return false;
			}
		}

		return true;
	}

	public RoleMappingForEmployee getRoleMappingsForEmployee(BigDecimal employeeId, String ipAddress, String deviceId,
			Boolean filterRole) {
		RoleMappingForEmployee rmForEmployee = new RoleMappingForEmployee();

		// put Employee Info by Employee Id
		FSEmployee employee = rbaacDao.getEmployeeByEmployeeId(employeeId);
		Map<BigDecimal, EmployeeDetailsDTO> employeeInfoMap = new HashMap<BigDecimal, EmployeeDetailsDTO>();
		if (employee != null) {
			List<BigDecimal> empIdList = new ArrayList<BigDecimal>();

			empIdList.add(employee.getEmployeeId());

			employeeInfoMap.put(employee.getEmployeeId(), ObjectConverter.convertEmployeeToEmpDetailsDTO(employee));
			rmForEmployee.setEmployeeInfoMap(employeeInfoMap);
		} else {
			rmForEmployee.setEmployeeInfoMap(employeeInfoMap);
		}

		// put role mapping list for that particular Employee Id
		UserRoleMapping userRoleMappingList = rbaacDao.getUserRoleMappingsByEmployeeId(employeeId);
		Map<BigDecimal, UserRoleMappingDTO> userRoleMappingInfoMap = new HashMap<BigDecimal, UserRoleMappingDTO>();
		if (userRoleMappingList != null) {
			userRoleMappingInfoMap.put(userRoleMappingList.getEmployeeId(),
					ObjectConverter.convertUrmToUrmDTO(userRoleMappingList));
			rmForEmployee.setUserRoleMappingInfoMap(userRoleMappingInfoMap);
		} else {
			rmForEmployee.setUserRoleMappingInfoMap(userRoleMappingInfoMap);
		}

		// put the role list
		List<Role> roleListAll = rbaacDao.getAllRoles();
		Map<BigDecimal, RoleResponseDTO> roleInfoMap = new HashMap<BigDecimal, RoleResponseDTO>();

		for (Role role : roleListAll) {
			if (userRoleMappingList != null && filterRole) {
				if (role.getId().compareTo(userRoleMappingList.getRoleId()) == 0) {
					roleInfoMap.put(role.getId(), ObjectConverter.convertRoleToRoleResponseDTO(role));
				}
			} else {
				roleInfoMap.put(role.getId(), ObjectConverter.convertRoleToRoleResponseDTO(role));
			}
		}
		rmForEmployee.setRoleInfoMap(roleInfoMap);

		return rmForEmployee;
	}

}
