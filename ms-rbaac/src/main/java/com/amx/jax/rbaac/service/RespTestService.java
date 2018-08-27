/**
 * 
 */
package com.amx.jax.rbaac.service;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.rbaac.dao.LoginDao;
import com.amx.jax.rbaac.dbmodel.AccessType;
import com.amx.jax.rbaac.dbmodel.PermScope;
import com.amx.jax.rbaac.dbmodel.Permission;
import com.amx.utils.JsonUtil;

/**
 * @author abhijeet
 *
 */
@Service
public class RespTestService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerService.getLogger(RespTestService.class);

	/** The login dao. */
	@Autowired
	LoginDao loginDao;

	public String testGetUrlCall() {

		LOGGER.info("Test Get Url API ");

		List<AccessType> accessTypes = loginDao.getAllAccessTypes();

		List<PermScope> permScopes = loginDao.getAllScopes();

		List<Permission> permissions = loginDao.getAllPermissions();

		StringBuilder builder = new StringBuilder();

		builder.append("Access Types  ==> " + JsonUtil.toJson(accessTypes));

		builder.append("\n\n\n Scope  ==> " + JsonUtil.toJson(permScopes));

		builder.append("\n\n\n Permissions  ==> " + JsonUtil.toJson(permissions));

		return builder.toString();
	}

}
