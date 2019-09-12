package com.amx.amxlib.service;

import java.util.List;

import com.amx.amxlib.meta.model.AnnualIncomeRangeDTO;
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

		public static final String RESEND_EMAIL_LOGIN = "/resend-email-login";
		public static final String ANNUAL_TRANSACTION_LIMIT_RANGE = "/get-annual-transaction-limit-range";

		public static final String SAVE_ANNUAL_TRANSACTION_LIMIT = "/save-annual-transaction-limit";

		public static final String GET_ANNUAL_TRANSACTION_LIMIT = "/get-annual-transaction-limit";

	}

	public static class Params {

		public static final String IDENTITY_INT = "identityInt";
	}

	AmxApiResponse<CustomerModelResponse, Object> getCustomerModelResponse(String identityInt);

	AmxApiResponse<BoolRespModel, Object> saveCustomerSecQuestions(List<SecurityQuestionModel> securityQuestion);

	AmxApiResponse<CustomerModelResponse, Object> getCustomerModelResponse();

	AmxApiResponse<CustomerModelSignupResponse, Object> getCustomerModelSignupResponse(String identityInt);

	@ApiJaxStatus({JaxError.UPDATE_PWD_REQUIRED })
	AmxApiResponse<BoolRespModel, Object> updatePasswordCustomer(String identityInt, String resetPassword);

	AmxApiResponse<AnnualIncomeRangeDTO, Object> getAnnualTransactionLimitRange();

}
