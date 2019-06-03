package com.amx.jax.customer;

import java.text.ParseException;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.model.customer.CreateCustomerInfoRequest;
import com.amx.jax.model.customer.UploadCustomerKycRequest;
import com.amx.jax.model.customer.UploadCustomerKycResponse;
import com.amx.jax.model.request.customer.UpdateCustomerInfoRequest;

public interface ICustomerManagementController {
	public static class ApiPath {
		public static final String PREFIX = "/customer-management";
		public static final String CREATE_CUSTOMER = PREFIX + "/create-customer";
		public static final String UPDATE_CUSTOMER = PREFIX + "/update-customer";
		public static final String UPLOAD_CUSTOMER_KYC = PREFIX + "/upload-customer-kyc";
		public static final String UPLOAD_CUSTOMER_DOCUMENT = PREFIX + "/upload-customer-document";
	}

	public static class ApiParams {
		public static final String IDENTITY = "identity";
		public static final String CUSTOMER_ID = "customerId";
		public static final String CONTACT_TYPE = "contactType";
		public static final String VERIFICATION_CODE = "code";
		public static final String LINK_ID = "linkId";
		public static final String CONTACT = "contact";
	}

	AmxApiResponse<BoolRespModel, Object> createCustomer(CreateCustomerInfoRequest createCustomerRequest) throws ParseException;

	AmxApiResponse<BoolRespModel, Object> updateCustomer(UpdateCustomerInfoRequest createCustomerRequest);

	AmxApiResponse<UploadCustomerKycResponse, Object> uploadCustomerKyc(UploadCustomerKycRequest uploadCustomerKycRequest);

}
