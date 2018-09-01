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
import com.amx.jax.rbaac.dbmodel.Permission;
import com.amx.jax.rbaac.dbmodel.Role;
import com.amx.jax.rbaac.dto.request.RoleRequestDTO;
import com.amx.jax.rbaac.dto.response.PermissionResposeDTO;
import com.amx.jax.rbaac.dto.response.RoleResponseDTO;
import com.amx.jax.rbaac.error.RbaacServiceError;
import com.amx.jax.rbaac.exception.AuthServiceException;
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

			RoleResponseDTO roleResponseDTO = new RoleResponseDTO();

			roleResponseDTO.setId(role.getId());
			roleResponseDTO.setRole(role.getRole());
			roleResponseDTO.setPermissionMap(roleRequestDTO.getPermissionMap());
			roleResponseDTO.setSuspended(role.getSuspended());
			roleResponseDTO.setFlags(role.getFlags());
			roleResponseDTO.setInfo(role.getInfo());
			roleResponseDTO.setCreatedDate(role.getCreatedDate());
			roleResponseDTO.setUpdatedDate(role.getUpdatedDate());

			return roleResponseDTO;

		}
	}

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
