package com.amx.jax.rbaac.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.rbaac.dao.LoginDao;
import com.amx.jax.rbaac.dbmodel.Permission;
import com.amx.jax.rbaac.dto.response.PermissionsResposeDTO;
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
	LoginDao loginDao;

	
	/**
	 * Gets the all permissions.
	 *
	 * @param ipAddr the ip addr
	 * @param deviceId the device id
	 * @return the all permissions
	 */
	public List<PermissionsResposeDTO> getAllPermissions(String ipAddr, String deviceId) {

		LOGGER.info("Get All Perms API Called");

		List<Permission> permissionList = loginDao.getAllPermissions();

		List<PermissionsResposeDTO> permDTOList = new ArrayList<PermissionsResposeDTO>();

		for (Permission permission : permissionList) {
			PermissionsResposeDTO permissionsResposeDTO = new PermissionsResposeDTO();

			permissionsResposeDTO.setId(permission.getId());
			permissionsResposeDTO.setPermission(permission.getPermission());
			permissionsResposeDTO.setContext(permission.getContext());
			permissionsResposeDTO.setFlags(permission.getFlags());
			permissionsResposeDTO.setInfo(permission.getInfo());

			try {
				permissionsResposeDTO.setAccessList(JsonUtil.getListFromJsonString(permission.getAccessListJson()));
				permissionsResposeDTO.setScopeList(JsonUtil.getListFromJsonString(permission.getScopeListJson()));
			} catch (IOException e) {

				throw new AuthServiceException("Incompatible JSON Data Type", AuthServiceError.INCOMPATIBLE_DATA_TYPE,
						e);

			}

			permDTOList.add(permissionsResposeDTO);

		}

		return permDTOList;
	}

}
