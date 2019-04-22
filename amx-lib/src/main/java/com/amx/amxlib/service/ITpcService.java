package com.amx.amxlib.service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.model.response.tpc.TpcGenerateClientSecretResponse;

/**
 * Third Party Client - service
 * 
 * @author prashant
 *
 */
public interface ITpcService {

	public static class Path {
		public static final String GENERATE_SECRET = "/generate-client-secret";
		public static final String VALIDATE_SECRET = "/validate-client-secret";
		public static final String SERVICE_PREFIX = "/tpc";
	}

	public static class Params {
		public static final String CLIENT_ID = "client-id";
	}

	AmxApiResponse<TpcGenerateClientSecretResponse, Object> generateSecret(String clientId, String actualSecret);

}
