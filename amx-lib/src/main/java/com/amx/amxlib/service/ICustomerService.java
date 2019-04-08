package com.amx.amxlib.service;

import java.util.List;

import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.model.response.customer.CustomerModelResponse;

public interface ICustomerService {
	
	public static class Path {
		
		
		public static final String CUSTOMER_MODEL_RESPONSE_GET = "/get-customer-model-response";
	}

	public static class Params {
		
		
		public static final String IDENTITY_INT = "identityInt";
	}

	AmxApiResponse<CustomerModelResponse, Object> getCustomerModelResponse(String identityInt);

	AmxApiResponse<BoolRespModel, Object> saveCustomerSecQuestions(List<SecurityQuestionModel> securityQuestion);

	AmxApiResponse<CustomerModelResponse, Object> getCustomerModelResponse();

}
