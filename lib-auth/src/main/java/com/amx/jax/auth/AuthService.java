package com.amx.jax.auth;

import java.math.BigDecimal;

import com.amx.amxlib.model.SendOtpModel;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.auth.meta.model.EmployeeDetailsDTO;
import com.amx.jax.auth.models.PermScope;
import com.amx.jax.auth.models.Permission;

public interface AuthService {

	AmxApiResponse<BoolRespModel, Object> saveEnums();

	AmxApiResponse<SendOtpModel, Object> verifyUserDetails(String empCode, String identity, String ipaddress);

	AmxApiResponse<EmployeeDetailsDTO, Object> verifyUserOTPDetails(String empCode, String identity, String mOtp,
			String ipaddress);

	AmxApiResponse<BoolRespModel, Object> saveRoleMaster(String roleTitle);

	AmxApiResponse<BoolRespModel, Object> saveAssignPermToRole(BigDecimal roleId, Permission permission,
			PermScope permScope, String admin);

	AmxApiResponse<BoolRespModel, Object> saveAssignRoleToUser(BigDecimal roleId, BigDecimal userId);


}
