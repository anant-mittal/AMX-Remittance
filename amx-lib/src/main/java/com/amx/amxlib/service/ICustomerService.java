package com.amx.amxlib.service;

import java.util.List;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.error.JaxError;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.model.customer.SecurityQuestionModel;
import com.amx.jax.model.response.customer.CustomerModelResponse;
import com.amx.jax.model.response.customer.CustomerModelSignupResponse;

public interface ICustomerService {
	
	public static class Path {
		
		
		public static final String CUSTOMER_MODEL_RESPONSE_GET = "/get-customer-model-response";
		
		public static final String CUSTOMER_MODEL_RESPONSE_BY_IDENTITYINT = "/get-customer-model-response-identityint";
		
		public static final String CUSTOMER_MODEL_SIGNUP_RESPONSE_GET = "/get-customer-model-signup-response";
	}

	public static class Params {
		
		
		public static final String IDENTITY_INT = "identityInt";
	}

	AmxApiResponse<CustomerModelResponse, Object> getCustomerModelResponse(String identityInt);

	AmxApiResponse<BoolRespModel, Object> saveCustomerSecQuestions(List<SecurityQuestionModel> securityQuestion);

	AmxApiResponse<CustomerModelResponse, Object> getCustomerModelResponse();

	AmxApiResponse<CustomerModelSignupResponse, Object> getCustomerModelSignupResponse(String identityInt);
	
	@ApiJaxStatus({ JaxError.MISSING_CONTACT_TYPE, JaxError.MOTP_REQUIRED, JaxError.EOTP_REQUIRED, JaxError.DOTP_REQUIRED, 
		JaxError.INVALID_OTP })
	AmxApiResponse<BoolRespModel, Object> resetPasswordFlow(String identityInt, String resetPassword);

}
