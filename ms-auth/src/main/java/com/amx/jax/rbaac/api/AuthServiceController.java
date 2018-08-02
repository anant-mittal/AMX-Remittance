package com.amx.jax.rbaac.api;

import java.io.IOException;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.model.dto.SendOtpModel;
import com.amx.jax.postman.PostManException;
import com.amx.jax.rbaac.AuthService;
import com.amx.jax.rbaac.dto.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.UserDetailsDTO;
import com.amx.jax.rbaac.models.PermScope;
import com.amx.jax.rbaac.models.Permission;
import com.amx.jax.rbaac.service.AuthServiceImpl;

import io.swagger.annotations.ApiOperation;

@RestController
public class AuthServiceController implements AuthService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceController.class);

	@Autowired
	AuthServiceImpl authService;

	/**
	 * @task Sync DB perms
	 */
	@ApiOperation("Sync Permissions")
	@RequestMapping(value = ApiEndPoints.SYNC_PERMS, method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> saveEnums() {
		// modules enums
		// permissions enums
		// perm scope enums
		// perm type enums
		return authService.saveEnums();

	}

	@ApiOperation("User Auth")
	@RequestMapping(value = ApiEndPoints.USER_VALID, method = RequestMethod.POST)
	public AmxApiResponse<SendOtpModel, Object> verifyUserDetails(@RequestParam String empCode,
			@RequestParam String identity, @RequestParam String ipAddress) {
		// point 1 : need to fetch employee details based on empCode
		// point 2 : need to check identity by empCode data
		// point 3 : once success, sending OTP to staff
		// point 4 : finally success or fail need to json object
		return authService.verifyUserDetails(empCode, identity, ipAddress);

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
	@RequestMapping(value = ApiEndPoints.USER_AUTH, method = RequestMethod.GET)
	public AmxApiResponse<EmployeeDetailsDTO, Object> verifyUserOTPDetails(@RequestParam String empCode,
			@RequestParam String identity, @RequestParam(required = false) String mOtp,
			@RequestParam(required = false) String ipaddress) {

		LOGGER.info("employee code : " + empCode + " identity : " + identity + " mOtp : " + mOtp + "ipaddress :"
				+ ipaddress);

		// point 1 : need to fetch employee details based on empCode
		// point 2 : need to check identity by empCode data
		// point 3 : once success of checking data OTP verification need to check
		// point 4 : finally success or fail need to json object

		return authService.verifyUserOTPDetails(empCode, identity, mOtp, ipaddress);

	}

	/**
	 * @task create role : role_name
	 * 
	 * @param roleTitle
	 */
	@ApiOperation("Create Role")
	@RequestMapping(value = ApiEndPoints.ROLE, method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> saveRoleMaster(@RequestParam String roleTitle) {
		return authService.saveRoleMaster(roleTitle);
	}

	/**
	 * @task assign_perms : role_id, permis_id, scope_id
	 * 
	 * @param roleId
	 * @param permission
	 * @param permScope
	 */
	@ApiOperation("Assign perms to Role")
	@RequestMapping(value = ApiEndPoints.ROLE_PERM, method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> saveAssignPermToRole(@RequestParam BigDecimal roleId,
			@RequestParam Permission permission, @RequestParam(required = false) PermScope permScope,
			@RequestParam(required = false) String admin) {
		return authService.saveAssignPermToRole(roleId, permission, permScope, admin);
	}

	/**
	 * @task assign_role : user_id, role_id
	 * 
	 * @param roleId
	 * @param userId
	 */
	@ApiOperation("Assign Role to user")
	@RequestMapping(value = ApiEndPoints.USER_ROLE, method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> saveAssignRoleToUser(@RequestParam BigDecimal roleId,
			@RequestParam BigDecimal userId) {
		return authService.saveAssignRoleToUser(roleId, userId);
	}

	/**
	 * @task get_perms : user_id -> role, list of permsissions +scope,
	 *       employe_detail,
	 * 
	 * @param userId
	 */
	@ApiOperation("Fetch User permissions")
	@RequestMapping(value = ApiEndPoints.USER_PERMS, method = RequestMethod.GET)
	public AmxApiResponse<UserDetailsDTO, Object> fetchUserMasterDetails(@RequestParam BigDecimal userId) {
		return authService.fetchUserMasterDetails(userId);
	}

}
