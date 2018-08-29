package com.amx.jax.rbaac.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.rbaac.dao.RbaacDao;
import com.amx.jax.rbaac.dbmodel.Permission;
import com.amx.jax.rbaac.dbmodel.Role;
import com.amx.jax.rbaac.dto.response.PermissionResposeDTO;
import com.amx.jax.rbaac.dto.response.RoleResponseDTO;
import com.amx.jax.rbaac.error.AuthServiceError;
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

				throw new AuthServiceException("Incompatible JSON Data Type", AuthServiceError.INCOMPATIBLE_DATA_TYPE,
						e);

			}

			permDTOList.add(permissionResposeDTO);

		}

		return permDTOList;
	}

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
	
	

}
