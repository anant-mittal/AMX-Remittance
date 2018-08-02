package com.amx.jax.rbaac;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.dto.SendOtpModel;
import com.amx.jax.rbaac.dto.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.UserDetailsDTO;
import com.amx.jax.rbaac.models.PermScope;
import com.amx.jax.rbaac.models.Permission;
import com.amx.jax.rest.RestService;

@Component
public class AuthServiceClient implements AuthService {

	private static final Logger LOGGER = LoggerService.getLogger(AuthServiceClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<BoolRespModel, Object> saveEnums() {
		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.SYNC_PERMS).post()
				.asApiResponse(BoolRespModel.class);
	}

	/**
	 * generate otp with employee details matching
	 */
	@Override
	public AmxApiResponse<SendOtpModel, Object> verifyUserDetails(String empCode, String identity, String ipaddress) {
		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.USER_VALID).queryParam("empCode", empCode)
				.queryParam("identity", identity).queryParam("ipaddress", ipaddress).get()
				.asApiResponse(SendOtpModel.class);
	}

	@Override
	public AmxApiResponse<EmployeeDetailsDTO, Object> verifyUserOTPDetails(String empCode, String identity, String mOtp,
			String ipaddress) {
		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.USER_AUTH).queryParam("empCode", empCode)
				.queryParam("identity", identity).queryParam("mOtp", mOtp).queryParam("ipaddress", ipaddress).get()
				.asApiResponse(EmployeeDetailsDTO.class);
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> saveRoleMaster(String roleTitle) {
		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.ROLE).queryParam("roleTitle", roleTitle)
				.post().asApiResponse(BoolRespModel.class);
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> saveAssignPermToRole(BigDecimal roleId, Permission permission,
			PermScope permScope, String admin) {
		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.ROLE_PERM).queryParam("roleId", roleId)
				.queryParam("permission", permission).queryParam("permScope", permScope).queryParam("admin", admin)
				.post().asApiResponse(BoolRespModel.class);
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> saveAssignRoleToUser(BigDecimal roleId, BigDecimal userId) {
		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.USER_ROLE).queryParam("roleId", roleId)
				.queryParam("userId", userId).post().asApiResponse(BoolRespModel.class);
	}

	@Override
	public AmxApiResponse<UserDetailsDTO, Object> fetchUserMasterDetails(BigDecimal userId) {
		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.USER_PERMS).queryParam("userId", userId)
				.post().asApiResponse(UserDetailsDTO.class);
	}

}
