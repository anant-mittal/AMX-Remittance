package com.amx.jax.proto.tpc.models;

import com.amx.jax.proto.tpc.api.TPCApiConstants;
import com.amx.jax.swagger.ApiMockModelProperty;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerAuth {

	public static class CustomerAuthRequest {

		@ApiMockModelProperty(
				value = "Gloabl IP of System/Device being used by Customer",
				example = "10.32.45.56",
				required = true)
		public String customerDeviceIp;

		@ApiMockModelProperty(
				value = "CPR id of customer",
				example = "284052306594",
				required = true)
		public String identity;

		@ApiMockModelProperty(
				value = "Mobile number of customer",
				example = "+965 881221212",
				required = true)
		public String mobile;
	}

	public static class CustomerAuthResponse {
		@ApiMockModelProperty(
				value = "Client is supposed to save this id for customer session.",
				example = "xwe2323w82323")
		@JsonProperty(TPCApiConstants.Keys.CUSTOMER_SESSION_TOKEN_XKEY)
		public String customerToken;

		@Deprecated
		@ApiMockModelProperty(
				value = "Client is supposed redirect cusotmer to this URL, where custoemr auth will take place",
				example = "https://appd-bhr.modernexchange.com/tpc/auth/customer")
		private String redirectUrl;
	}
}
