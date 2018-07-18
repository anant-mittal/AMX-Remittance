package com.amx.jax.branch.api;

import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.auth.AuthConstants;
import com.amx.jax.auth.models.PermScope;
import com.amx.jax.auth.models.Permission;
import com.amx.jax.branch.service.CustRegService;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.postman.PostManException;

import io.swagger.annotations.ApiOperation;

@RestController
public class CustRegController {

	private static final Logger LOGGER = LoggerService.getLogger(CustRegController.class);

	@Autowired
	CustRegService authServiceImpl;

	/**
	 * @task Sync DB perms
	 */
	@ApiOperation("Sync Permissions")
	@RequestMapping(value = AuthConstants.ApiEndPoints.SYNC_PERMS, method = RequestMethod.POST)
	public void syncPermsMeta() {

	}

	/**
	 * @task send_otp:ec_no, civilid verify_otp:ec_no,
	 * @task civilid, motp -> user_id
	 * 
	 * @description if mOtp is missng send OTP else validate ,
	 * 
	 * @param empCode
	 * @param identity
	 * @param mOtp
	 * @throws PostManException
	 * @throws IOException
	 */
	@ApiOperation("User Auth")
	@RequestMapping(value = AuthConstants.ApiEndPoints.USER_AUTH, method = RequestMethod.GET)
	public void authUser(@RequestParam String empCode, @RequestParam String identity,
			@RequestParam(required = false) String mOtp) {

	}

	/**
	 * @task create role : role_name
	 * 
	 * @param roleTitle
	 */
	@ApiOperation("Create Role")
	@RequestMapping(value = AuthConstants.ApiEndPoints.ROLE, method = RequestMethod.POST)
	public void createRole(@RequestParam String roleTitle) {

	}

	/**
	 * @task assign_perms : role_id, permis_id, scope_id
	 * 
	 * @param roleId
	 * @param permission
	 * @param permScope
	 */
	@ApiOperation("Assign perms to Role")
	@RequestMapping(value = AuthConstants.ApiEndPoints.ROLE_PERM, method = RequestMethod.POST)
	public void assinPerm(@RequestParam String roleId, @RequestParam Permission permission,
			@RequestParam(required = false) PermScope permScope) {

	}

	/**
	 * @task assign_role : user_id, role_id
	 * 
	 * @param roleId
	 * @param userId
	 */
	@ApiOperation("Assign Role to user")
	@RequestMapping(value = AuthConstants.ApiEndPoints.USER_ROLE, method = RequestMethod.POST)
	public void assinRole(@RequestParam String roleId, @RequestParam String userId) {

	}

	/**
	 * @task get_perms : user_id -> role, list of permsissions +scope,
	 *       employe_detail,
	 * 
	 * @param userId
	 */
	@ApiOperation("Fetch User permissions")
	@RequestMapping(value = AuthConstants.ApiEndPoints.USER_PERMS, method = RequestMethod.GET)
	public void getUserPerms(@RequestParam String userId) {

	}

}
