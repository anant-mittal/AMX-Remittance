package com.amx.jax.customer;

import java.text.ParseException;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.model.customer.CreateCustomerInfoRequest;
import com.amx.jax.model.customer.DuplicateCustomerDto;
import com.amx.jax.model.customer.IdentityTypeDto;
import com.amx.jax.model.customer.document.CustomerDocumentCategoryDto;
import com.amx.jax.model.customer.document.CustomerDocumentTypeDto;
import com.amx.jax.model.customer.document.UploadCustomerDocumentRequest;
import com.amx.jax.model.customer.document.UploadCustomerDocumentResponse;
import com.amx.jax.model.customer.document.UploadCustomerKycRequest;
import com.amx.jax.model.customer.document.UploadCustomerKycResponse;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.customer.UpdateCustomerInfoRequest;
import com.amx.jax.model.response.CustomerInfo;
import com.amx.libjax.model.jaxfield.JaxFieldDto;

public interface ICustomerManagementController {
	public static class ApiPath {
		public static final String PREFIX = "/customer-management";
		public static final String CREATE_CUSTOMER = PREFIX + "/create-customer";
		public static final String UPDATE_CUSTOMER = PREFIX + "/update-customer";
		public static final String UPLOAD_CUSTOMER_KYC = PREFIX + "/upload-customer-kyc";
		public static final String UPLOAD_CUSTOMER_DOCUMENT = PREFIX + "/upload-customer-document";
		public static final String DUPLICATE_CUSTOMER_CHECK = PREFIX + "/duplicate-cusotmer-check";
		public static final String GET_IDENTITY_TPYES = PREFIX + "/get-identity-types";
		public static final String LOCK_ONLINE_CUSTOMER = PREFIX + "/lock-online-customer";
		public static final String UNLOCK_ONLINE_CUSTOMER = PREFIX + "/unlock-online-customer";
		public static final String DOCUMENT_CATEGORY_GET = PREFIX + "/list-document-category";
		public static final String DOCUMENT_TYPE_GET = PREFIX + "/list-document-type";
		public static final String DOCUMENT_FIELD_GET = PREFIX + "/list-document-fields";
		public static final String DOCUMENT_DYNAMIC_FIELD_LIST = PREFIX + "/get-document-dynamic-field-list";
	}

	public static class ApiParams {
		public static final String IDENTITY = "identity";
		public static final String CUSTOMER_ID = "customerId";
		public static final String CONTACT_TYPE = "contactType";
		public static final String VERIFICATION_CODE = "code";
		public static final String LINK_ID = "linkId";
		public static final String CONTACT = "contact";
		public static final String DOCUMENT_CATEGORY = "document-category";
		public static final String DOCUMENT_TYPE = "document-type";
	}

	AmxApiResponse<CustomerInfo, Object> createCustomer(CreateCustomerInfoRequest createCustomerRequest) throws ParseException;

	AmxApiResponse<BoolRespModel, Object> updateCustomer(UpdateCustomerInfoRequest createCustomerRequest) throws ParseException;

	AmxApiResponse<UploadCustomerKycResponse, Object> uploadCustomerKyc(UploadCustomerKycRequest uploadCustomerKycRequest);

	AmxApiResponse<DuplicateCustomerDto, Object> checkForDuplicateCustomer(CustomerPersonalDetail customerPersonalDetail);

	AmxApiResponse<IdentityTypeDto, Object> getIdentityTypes();

	AmxApiResponse<BoolRespModel, Object> lockOnlineCustomer();

	AmxApiResponse<BoolRespModel, Object> unlockOnlineCustomer();

	AmxApiResponse<UploadCustomerDocumentResponse, Object> uploadCustomerDocument(UploadCustomerDocumentRequest uploadCustomerDocumentRequest);

	AmxApiResponse<CustomerDocumentCategoryDto, Object> getDocumentCategory();

	AmxApiResponse<CustomerDocumentTypeDto, Object> getDocumentType(String documentCategory);

	AmxApiResponse<JaxFieldDto, Object> getDocumentFields(String documentCategory, String documentType);

}
