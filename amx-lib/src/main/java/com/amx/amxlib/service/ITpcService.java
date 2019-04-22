package com.amx.amxlib.service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;

/**
 * Third Party Client - service
 * 
 * @author prashant
 *
 */
public interface ITpcService {

	public static class Path {
		public static final String GENERATE_SECRET = "/generate-client-secret";
		public static final String SERVICE_PREFIX = "/tpc";
	}

	public static class Params {
		public static final String CLIENT_ID = "client-id";
	}

	AmxApiResponse<BoolRespModel, Object> generateSecret(String clientId, String actualSecret);

}
