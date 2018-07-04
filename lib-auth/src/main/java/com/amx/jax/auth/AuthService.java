package com.amx.jax.auth;

import java.math.BigDecimal;

import com.amx.amxlib.model.SendOtpModel;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.auth.dto.EmployeeDetailsDTO;
import com.amx.jax.auth.dto.UserDetailsDTO;
import com.amx.jax.auth.models.PermScope;
import com.amx.jax.auth.models.Permission;

public interface AuthService {

	public static class ApiEndPoints {
		public static final String SYNC_PERMS = "/api/sync/perms/";
		public static final String USER_VALID = "/api/user/validate/";
		public static final String USER_AUTH = "/api/user/auth/";
		public static final String ROLE = "/api/role/";
		public static final String ROLE_PERM = "/api/role/perm/";
		public static final String USER_ROLE = "/api/user/role/";
		public static final String USER_PERMS = "/api/user/perms/";
	}

	public AmxApiResponse<BoolRespModel, Object> saveEnums();

	public AmxApiResponse<SendOtpModel, Object> verifyUserDetails(String empCode, String identity, String ipaddress);

	public AmxApiResponse<EmployeeDetailsDTO, Object> verifyUserOTPDetails(String empCode, String identity, String mOtp,
			String ipaddress);

	public AmxApiResponse<BoolRespModel, Object> saveRoleMaster(String roleTitle);

	public AmxApiResponse<BoolRespModel, Object> saveAssignPermToRole(BigDecimal roleId, Permission permission,
			PermScope permScope, String admin);

	public AmxApiResponse<BoolRespModel, Object> saveAssignRoleToUser(BigDecimal roleId, BigDecimal userId);

	public AmxApiResponse<UserDetailsDTO, Object> fetchUserMasterDetails(BigDecimal userId);

}
