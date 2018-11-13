package com.amx.jax.proto.tpc.models;

import com.amx.jax.proto.tpc.api.TPCApiConstants;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class ClientAuth {

	public static class ClientAuthRequest {
		@ApiModelProperty(example = "sadad2323", value = "Client Id provided by AMX",
			required = true)
		public String clientId;

		@ApiModelProperty(example = "1d4zw0xv8gx6q", value = "Client Secret provided by AMX",
			required = true)
		public String clientSecret;
	}

	public static class ClientAuthResponse {

		@ApiModelProperty(example = "1d4zw0x6ygg75", value = "Session Id valid for certain period.",
			required = true)
		@JsonProperty(TPCApiConstants.Keys.CLIENT_SESSION_TOKEN_XKEY)
		public String sessionId;
	}
}
