package com.amx.jax.rbaac.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.rbaac.dao.RbaacDao;
import com.amx.jax.rbaac.dbmodel.Employee;
import com.amx.jax.rbaac.dbmodel.Permission;
import com.amx.jax.rbaac.dbmodel.Role;
import com.amx.jax.rbaac.dbmodel.UserRoleMapping;
import com.amx.jax.rbaac.dto.request.RoleRequestDTO;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.response.PermissionResposeDTO;
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
						throw new AuthServiceException("Duplicate Role", RbaacServiceError.DUPLICATE_ROLE);
					}
				}

				role = new Role();
				role.setId(new BigDecimal(0));
				role.setCreatedDate(today);

			} else { // Old Role is Updated

				role = rbaacDao.getRoleById(roleRequestDTO.getId());

				if (role == null) {
					throw new AuthServiceException("Invalid Role: No Role Exists with Given Id",
							RbaacServiceError.INVALID_ROLE_DEFINITION);
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

	public UserRoleMappingsResponseDTO getUserRoleMappingsForBranch(BigDecimal countryBranchId, String ipAddr, String deviceId) {

		UserRoleMappingsResponseDTO urmResponseDTO = new UserRoleMappingsResponseDTO();

		List<Employee> employeeList = rbaacDao.getEmployeesByCountryBranchId(countryBranchId);

		if (employeeList == null || employeeList.isEmpty()) {
			return urmResponseDTO;
		}

		Map<BigDecimal, EmployeeDetailsDTO> employeeInfoMap = new HashMap<BigDecimal, EmployeeDetailsDTO>();

		List<BigDecimal> empIdList = new ArrayList<BigDecimal>();

		for (Employee employee : employeeList) {
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
						throw new AuthServiceException(
								"Invalid Permission: One or more permissions are Invalid: " + entry.getKey() + " : "
										+ innerEntry.getKey() + " : " + innerEntry.getValue(),
								RbaacServiceError.INVALID_ACCESS_TYPE_SCOPE);
					}
				}

			} else {
				throw new AuthServiceException(
						"Invalid Permission: One or more permissions are Invalid: " + entry.getKey(),
						RbaacServiceError.INVALID_PERMISSION);
			}

		} // for

		return true;

	}

}
