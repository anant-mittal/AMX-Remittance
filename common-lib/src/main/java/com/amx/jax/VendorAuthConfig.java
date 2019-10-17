package com.amx.jax;

import org.springframework.stereotype.Component;

import com.amx.jax.http.CommonHttpRequest;
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

	@VendorValue("${basic.auth.ip}")
	String basicAuthIp;

	public String getBasicAuthIp() {
		return basicAuthIp;
	}

	public String getBasicAuthPassword() {
		return basicAuthPassword;
	}

	public String getBasicAuthUser() {
		return basicAuthUser;
	}

	public boolean isRequestValid(ApiRequestDetail apiRequest, CommonHttpRequest req, String traceId,
			String authToken) {
		return authToken.equals(basicAuthPassword)
				|| CryptoUtil.validateHMAC(this.basicAuthPassword, traceId, authToken);
	}

}
