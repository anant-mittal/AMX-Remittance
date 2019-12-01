package com.amx.jax.rest;

import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.http.CommonHttpRequest.ApiRequestDetail;

public final class AppRequestInterfaces {
	public interface VendorAuthFilter {
		public boolean isAuthVendorRequest(ApiRequestDetail apiRequest, CommonHttpRequest req, String traceId,
				String authToken);
	}
}
