package com.amx.jax.auth.api;

import static com.amx.jax.auth.AuthConstants.ApiEndPoints.ROLE;
import static com.amx.jax.auth.AuthConstants.ApiEndPoints.ROLE_PERM;
import static com.amx.jax.auth.AuthConstants.ApiEndPoints.SYNC_PERMS;
import static com.amx.jax.auth.AuthConstants.ApiEndPoints.USER_AUTH;
import static com.amx.jax.auth.AuthConstants.ApiEndPoints.USER_PERMS;
import static com.amx.jax.auth.AuthConstants.ApiEndPoints.USER_ROLE;
import static com.amx.jax.auth.AuthConstants.ApiEndPoints.USER_VALID;

import java.io.IOException;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.auth.manager.AuthLoginManager;
import com.amx.jax.auth.models.PermScope;
import com.amx.jax.auth.models.Permission;
import com.amx.jax.auth.service.LoginService;
import com.amx.jax.postman.PostManException;

import io.swagger.annotations.ApiOperation;

@RestController
public class AuthServiceController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceController.class);

	@Autowired
	LoginService loginService;
	
	@Autowired
	AuthLoginManager authLoginManager;

	/**
	 * @task Sync DB perms
	 */
	@ApiOperation("Sync Permissions")
	@RequestMapping(value = SYNC_PERMS, method = RequestMethod.POST)
	public ApiResponse syncPermsMeta() {
		
		// modules enums
		// permissions enums
		// perm scope enums
		// perm type enums
		
		ApiResponse status = loginService.saveEnums();
		System.out.println("status : " + status);
		
		return status;
	}
	
	@ApiOperation("User Auth")
	@RequestMapping(value = USER_VALID, method = RequestMethod.GET)
	public ApiResponse validateUser(@RequestParam String empCode, @RequestParam String identity,@RequestParam(required = false) String ipaddress) {
		
		LOGGER.info("employee code : "+empCode + " identity : "+identity+ "ipaddress :" +ipaddress);
		ApiResponse apiResponse = null;
		
		// point 1 : need to fetch employee details based on empCode
		// point 2 : need to check identity by empCode data
		// point 3 : once success, sending OTP to staff
		// point 4 : finally success or fail need to json object
		
		apiResponse = loginService.verifyUserDetails(empCode, identity,ipaddress);
		
		return apiResponse;

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
	@RequestMapping(value = USER_AUTH, method = RequestMethod.GET)
	public ApiResponse authUser(@RequestParam String empCode, @RequestParam String identity,
			@RequestParam(required = false) String mOtp,@RequestParam(required = false) String ipaddress) {
		
		LOGGER.info("employee code : "+empCode + " identity : "+identity + " mOtp : " + mOtp + "ipaddress :" +ipaddress);
		ApiResponse apiResponse = null;
		
		// point 1 : need to fetch employee details based on empCode
		// point 2 : need to check identity by empCode data
		// point 3 : once success of checking data OTP verification need to check
		// point 4 : finally success or fail need to json object
		
		apiResponse = loginService.verifyUserOTPDetails(empCode, identity,mOtp,ipaddress);
		
		return apiResponse;

	}

	/**
	 * @task create role : role_name
	 * 
	 * @param roleTitle
	 */
	@ApiOperation("Create Role")
	@RequestMapping(value = ROLE, method = RequestMethod.POST)
	public ApiResponse createRole(@RequestParam String roleTitle) {
		ApiResponse status = loginService.saveRoleMaster(roleTitle);
		System.out.println("status : " + status);
		return status;
	}

	/**
	 * @task assign_perms : role_id, permis_id, scope_id
	 * 
	 * @param roleId
	 * @param permission
	 * @param permScope
	 */
	@ApiOperation("Assign perms to Role")
	@RequestMapping(value = ROLE_PERM, method = RequestMethod.POST)
	public ApiResponse assinPerm(@RequestParam BigDecimal roleId, @RequestParam Permission permission,
			@RequestParam(required = false) PermScope permScope,@RequestParam(required = false) String admin) {
		ApiResponse status = loginService.saveAssignPermToRole(roleId,permission,permScope,admin);
		System.out.println("status : " + status);
		return status;
	}

	/**
	 * @task assign_role : user_id, role_id
	 * 
	 * @param roleId
	 * @param userId
	 */
	@ApiOperation("Assign Role to user")
	@RequestMapping(value = USER_ROLE, method = RequestMethod.POST)
	public ApiResponse assinRole(@RequestParam BigDecimal roleId, @RequestParam BigDecimal userId) {
		ApiResponse status = loginService.saveAssignRoleToUser(roleId,userId);
		System.out.println("status : " + status);
		return status;
	}

	/**
	 * @task get_perms : user_id -> role, list of permsissions +scope,
	 *       employe_detail,
	 * 
	 * @param userId
	 */
	@ApiOperation("Fetch User permissions")
	@RequestMapping(value = USER_PERMS, method = RequestMethod.GET)
	public ApiResponse getUserPerms(@RequestParam BigDecimal userId) {
		ApiResponse user = loginService.fetchUserMasterDetails(userId);
		System.out.println(user);
		return user;
	}
	
}
