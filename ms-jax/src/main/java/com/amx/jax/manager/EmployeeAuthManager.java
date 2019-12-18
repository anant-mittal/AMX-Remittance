package com.amx.jax.manager;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.JaxAuthContext;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.dict.ContactType;
import com.amx.jax.error.JaxError;
import com.amx.jax.rbaac.RbaacServiceClient;
import com.amx.jax.rbaac.dto.response.OfflineOtpData;

/**
 * Jax Auth manager for employee to validate otp, password etc
 * 
 * @author prashant
 *
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EmployeeAuthManager {

	@Autowired
	RbaacServiceClient rbaacServiceClient;

	public void validateAndSendOtp(BigDecimal employeeId) {
		ContactType contactType = JaxAuthContext.getContactType();
		if (contactType == null) {
			GlobalException ex = new GlobalException(JaxError.CONTACT_TYPE_REQUIRED, "Contact Type is missing");
			throw ex;
		}
		// offline otp
		if (contactType.equals(ContactType.NOTP_APP)) {
			String notp = JaxAuthContext.getNotp();
			if (notp == null) {
				sendNotp(employeeId);
			} else {
				validateNopt(employeeId, notp);
			}
		}
	}

	private void validateNopt(BigDecimal employeeId, String notp) {
		AmxApiResponse<BoolRespModel, Object> validateOtpResp = rbaacServiceClient.validateOfflineOtp(employeeId, notp);
		if (!validateOtpResp.getResult().isSuccess()) {
			throw new GlobalException(JaxError.INVALID_OTP, "Otp is not valid or expired");
		}
	}

	private void sendNotp(BigDecimal employeeId) {
		AmxApiResponse<OfflineOtpData, Object> response = rbaacServiceClient.generateOfflineOtpPrefix(employeeId);
		OfflineOtpData responseData = response.getResult();
		GlobalException ex = new GlobalException(JaxError.NOTP_REQUIRED, "OTP required");
		ex.setMeta(responseData);
		throw ex;

	}
}
