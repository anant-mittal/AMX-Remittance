package com.amx.jax.client;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.model.request.tpc.TpcCustomerLoginRequest;
import com.amx.jax.model.response.customer.CustomerModelResponse;
import com.amx.jax.model.response.tpc.TpcGenerateClientSecretResponse;
import com.amx.jax.model.response.tpc.TpcValidateClientSecretResponse;

/**
 * Third Party Client - service
 * 
 * @author prashant
 *
 */
public interface ITpcService {

	public static class Path {

		public static final String SERVICE_PREFIX = "/tpc";
		public static final String GENERATE_SECRET = SERVICE_PREFIX + "/generate-tpc-client-secret";
		public static final String VALIDATE_SECRET = SERVICE_PREFIX + "/validate-tpc-client-secret";
		public static final String LOGIN_CUSTOMER = SERVICE_PREFIX + "/login-customer";
	}

	public static class Params {
		public static final String CLIENT_ID = "client-id";
		public static final String CLIENT_PASS = "client-pass";
		public static final String CLIENT_SECRET = "client-secret";
		public static final String CIVIL_ID = "civil-id";
		public static final String MOBILE = "mobile";
	}

	AmxApiResponse<TpcGenerateClientSecretResponse, Object> generateSecret(String clientId, String actualSecret);

	AmxApiResponse<TpcValidateClientSecretResponse, Object> validateSecret(String clientId, String clientSecret);

	AmxApiResponse<CustomerModelResponse, Object> loginCustomer(TpcCustomerLoginRequest tpcCustomerLoginRequest);

}
