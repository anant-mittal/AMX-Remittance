package com.amx.jax;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.CustomerInfoRequest;
import com.amx.jax.model.request.DynamicFieldRequest;
import com.amx.jax.model.request.EmploymentDetailsRequest;
import com.amx.jax.model.request.ImageSubmissionRequest;
import com.amx.jax.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.model.response.ArticleDetailsDescDto;
import com.amx.jax.model.response.ArticleMasterDescDto;
import com.amx.jax.model.response.ComponentDataDto;
import com.amx.jax.model.response.FieldListDto;
import com.amx.jax.model.response.IncomeRangeDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public interface ICustRegService {

	public static class CustRegApiEndPoints {
		public static final String PREFIX = "/offsite-cust-reg";
		public static final String GET_DYNAMIC_FIELDS = PREFIX + "/new-field-list";
		public static final String GET_ID_TYPES = PREFIX + "/send-id-types";
		public static final String GET_CUSTOMER_OTP = PREFIX + "/customer-mobile-email-send-otp";
		public static final String VALIDATE_OTP = PREFIX + "/customer-mobile-email-validate-otp";
		public static final String GET_ARTICLE_LIST = PREFIX + "/articleList";
		public static final String GET_DESIGNATION_LIST = PREFIX + "/designationList";
		public static final String GET_INCOME_RANGE_LIST = PREFIX + "/incomeRangeList";
		public static final String GET_EMPLOYMENT_TYPE_LIST = PREFIX + "/employmentTypeList";
		public static final String GET_PROFESSION_LIST = PREFIX + "/professionList";
		public static final String SAVE_CUST_INFO = PREFIX + "/saveCustomerInfo";
		public static final String SAVE_KYC_DOC = PREFIX + "/saveCustomerKYCDoc";
		public static final String SAVE_SIGNATURE = PREFIX + "/saveCustomerSignature";
	}

	@JsonDeserialize(as = CustRegRequestModel.class)
	public interface RegModeModel {
		public BigDecimal getMode();

		public void setMode(BigDecimal modeId);
	}

	AmxApiResponse<ComponentDataDto, Object> getIdTypes();

	AmxApiResponse<Map<String, FieldListDto>, Object> getFieldList(DynamicFieldRequest model);

	AmxApiResponse<IncomeRangeDto, Object> getIncomeRangeResponse(EmploymentDetailsRequest model);

	AmxApiResponse<ArticleDetailsDescDto, Object> getDesignationListResponse(EmploymentDetailsRequest model);

	AmxApiResponse<ArticleMasterDescDto, Object> getArticleListResponse();

	AmxApiResponse<String, Object> validateOtpForEmailAndMobile(OffsiteCustomerRegistrationRequest offsiteCustRegModel);

	AmxApiResponse<ComponentDataDto, Object> sendEmploymentTypeList();

	AmxApiResponse<ComponentDataDto, Object> sendProfessionList();

	AmxApiResponse<BigDecimal, Object> saveCustomerInfo(CustomerInfoRequest model);

	@ApiJaxStatus({ JaxError.IMAGE_NOT_AVAILABLE, JaxError.NULL_CUSTOMER_ID, JaxError.INVALID_CUSTOMER })
	AmxApiResponse<String, Object> saveCustomeKycDocument(ImageSubmissionRequest modelData) throws ParseException;

	AmxApiResponse<String, Object> saveCustomerSignature(ImageSubmissionRequest model);

}
