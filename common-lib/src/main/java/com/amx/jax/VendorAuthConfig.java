package com.amx.jax;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.amx.jax.http.CommonHttpRequest.ApiRequestDetail;
import com.amx.jax.scope.VendorContext.VendorScoped;
import com.amx.jax.scope.VendorContext.VendorValue;
import com.amx.utils.CryptoUtil;

@Component
@VendorScoped
public class VendorAuthConfig {

	@VendorValue("${basic.auth.key}")
	String basicAuthPassword;

	@VendorValue("${basic.auth.id}")
	String basicAuthUser;

	public String getBasicAuthPassword() {
		return basicAuthPassword;
	}

	public String getBasicAuthUser() {
		return basicAuthUser;
	}

	public boolean isRequestValid(ApiRequestDetail apiRequest, HttpServletRequest req, String traceId,
			String authToken) {
		return authToken.equals(basicAuthPassword)
				|| CryptoUtil.validateHMAC(this.basicAuthPassword, traceId, authToken);
	}

}
