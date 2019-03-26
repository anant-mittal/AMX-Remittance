package com.amx.jax.proto.tpc.models;

import java.math.BigDecimal;

import com.amx.jax.proto.tpc.api.TPCApiConstants;
import com.amx.jax.swagger.ApiMockModelProperty;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerAuth {

	public static class CustomerAuthRequest {

		@ApiMockModelProperty(
			value = "Redirect URL after customer auth is completed. Client is supposed to implement this api URL",
			example = "https://clientsite.com/amx/callback",
			required = true)
		public String callbackUrl;

		@ApiMockModelProperty(
			value = "Gloabl IP of System being used by Customer",
			example = "10.32.45.56",
			required = true)
		public String customerSystempIp;
	}

	public static class CustomerAuthResponse {
		@ApiMockModelProperty(
			value = "Client is supposed to save this id for customer session.",
			example = "34739847")
		@JsonProperty(TPCApiConstants.Keys.CUSTOMER_SESSION_TOKEN_XKEY)
		public BigDecimal customerToken;

		@ApiMockModelProperty(
			value = "Client is supposed redirect cusotmer to this URL, where custoemr auth will take place",
			example = "https://appd-bhr.modernexchange.com/tpc/auth/customer")
		public String redirectUrl;
	}
}
