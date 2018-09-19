package com.amx.jax;

import java.math.BigDecimal;
import java.util.Map;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.model.request.CustomerInfoRequest;
import com.amx.jax.model.request.DynamicFieldRequest;
import com.amx.jax.model.request.EmploymentDetailsRequest;
import com.amx.jax.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.model.response.ArticleDetailsDescDto;
import com.amx.jax.model.response.ArticleMasterDescDto;
import com.amx.jax.model.response.ComponentDataDto;
import com.amx.jax.model.response.FieldListDto;
import com.amx.jax.model.response.IncomeRangeDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public interface ICustRegService {

	public static class CustRegApiEndPoints {
		public static final String GET_MODES = "/api/reg/modes";
		public static final String GET_ID_FIELDS = "/api/reg/id/fields";
		public static final String GET_DYNAMIC_FIELDS = "/new-field-list";
		public static final String GET_ID_TYPES = "/send-id-types";
		public static final String GET_CUSTOMER_OTP = "/customer-mobile-email-send-otp";
		public static final String VALIDATE_OTP = "/customer-mobile-email-validate-otp";
		public static final String GET_ARTICLE_LIST = "/articleList";
		public static final String GET_DESIGNATION_LIST = "/designationList";
		public static final String GET_INCOME_RANGE_LIST = "/incomeRangeList";
		public static final String GET_EMPLOYMENT_TYPE_LIST = "/employmentTypeList";
		public static final String GET_PROFESSION_LIST = "/professionList";
		public static final String SAVE_CUST_INFO = "/saveCustomerInfo";
		public static final String SAVE_KYC_DOC = "/saveCustomerKYCDoc";
	}

	@JsonDeserialize(as = CustRegRequestModel.class)
	public interface RegModeModel {
		public BigDecimal getMode();

		public void setMode(BigDecimal modeId);
	}
	
	AmxApiResponse<ComponentDataDto, Object> sendIdTypes();
	
	AmxApiResponse<Map<String, FieldListDto>, Object> getFieldList(DynamicFieldRequest model);
	
	AmxApiResponse<IncomeRangeDto, Object> getIncomeRangeResponse(EmploymentDetailsRequest model);
	
	AmxApiResponse<ArticleDetailsDescDto, Object> getDesignationListResponse(EmploymentDetailsRequest model);
	
	AmxApiResponse<ArticleMasterDescDto, Object> getArticleListResponse();
	
	AmxApiResponse<String, Object> validateOtpForEmailAndMobile(OffsiteCustomerRegistrationRequest offsiteCustRegModel);
	
	AmxApiResponse<ComponentDataDto, Object> sendEmploymentTypeList();
	
	AmxApiResponse<ComponentDataDto, Object> sendProfessionList();
	
	AmxApiResponse<BigDecimal, Object> saveCustomerInfo(CustomerInfoRequest model);

}
