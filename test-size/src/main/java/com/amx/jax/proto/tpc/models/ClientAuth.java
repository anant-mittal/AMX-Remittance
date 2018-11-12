package com.amx.jax.proto.tpc.models;

import com.amx.jax.proto.tpc.api.TPCApiConstants;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientAuth {

	public static class ClientAuthRequest {
		public String clientId;
		public String clientSecret;
	}

	public static class ClientAuthResponse {
		@JsonProperty(TPCApiConstants.Keys.CLIENT_SESSION_TOKEN_XKEY)
		public String sessionId;
	}
}
