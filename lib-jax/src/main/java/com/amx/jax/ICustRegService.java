package com.amx.jax;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.CardDetail;
import com.amx.jax.model.dto.SendOtpModel;
import com.amx.jax.model.request.CustomerInfoRequest;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.DynamicFieldRequest;
import com.amx.jax.model.request.EmploymentDetailsRequest;
import com.amx.jax.model.request.ImageSubmissionRequest;
import com.amx.jax.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.model.response.ArticleDetailsDescDto;
import com.amx.jax.model.response.ArticleMasterDescDto;
import com.amx.jax.model.response.ComponentDataDto;
import com.amx.jax.model.response.CustomerInfo;
import com.amx.jax.model.response.FieldListDto;
import com.amx.jax.model.response.IncomeRangeDto;
import com.amx.jax.model.response.customer.OffsiteCustomerDataDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public interface ICustRegService extends IJaxService {

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
		public static final String SCAN_CARD = PREFIX + "/scan_card";
		public static final String SAVE_OFFSITE_LOGIN = PREFIX + "/offsite-save-login-detail";
		public static final String GET_OFFSITE_CUSTOMER_DATA = PREFIX + "/getOffsiteCustomerData";
		public static final String GET_CUSTOMER_DEATILS = PREFIX + "/customer-details";
	}
	
	public static class Params {
		public static final String IDENTITY_INT = "identityInt";
		public static final String IDENTITY_TYPE = "identityType";
	}

	@JsonDeserialize(as = CustRegRequestModel.class)
	public interface RegModeModel {
		public BigDecimal getMode();

		public void setMode(BigDecimal modeId);
	}

	@ApiJaxStatus({ JaxError.EMPTY_ID_TYPE_LIST })
	AmxApiResponse<ComponentDataDto, Object> getIdTypes();

	@ApiJaxStatus({ JaxError.EMPTY_FIELD_CONDITION })
	AmxApiResponse<Map<String, FieldListDto>, Object> getFieldList(DynamicFieldRequest model);

	@ApiJaxStatus({ JaxError.EMPTY_INCOME_RANGE })
	AmxApiResponse<IncomeRangeDto, Object> getIncomeRangeResponse(EmploymentDetailsRequest model);

	@ApiJaxStatus({ JaxError.EMPTY_DESIGNATION_LIST })
	AmxApiResponse<ArticleDetailsDescDto, Object> getDesignationListResponse(EmploymentDetailsRequest model);

	@ApiJaxStatus({ JaxError.EMPTY_ARTICLE_LIST })
	AmxApiResponse<ArticleMasterDescDto, Object> getArticleListResponse();

	@ApiJaxStatus({ JaxError.MISSING_OTP, JaxError.VALIDATE_OTP_LIMIT_EXCEEDED })
	AmxApiResponse<String, Object> validateOtpForEmailAndMobile(OffsiteCustomerRegistrationRequest offsiteCustRegModel);

	@ApiJaxStatus({ JaxError.EMPTY_EMPLOYMENT_TYPE })
	AmxApiResponse<ComponentDataDto, Object> sendEmploymentTypeList();

	@ApiJaxStatus({ JaxError.EMPTY_PROFESSION_LIST })
	AmxApiResponse<ComponentDataDto, Object> sendProfessionList();

	AmxApiResponse<CardDetail, Object> cardScan(CardDetail cardDetail);

	@ApiJaxStatus({ JaxError.EXISTING_CIVIL_ID, JaxError.EXISTING_BEDOUIN_ID, JaxError.EXISTING_GCC_ID,JaxError.EXISTING_PASSPORT, JaxError.INVALID_CIVIL_ID })
	AmxApiResponse<CustomerInfo, Object> saveCustomerInfo(CustomerInfoRequest model);

	@ApiJaxStatus({ JaxError.IMAGE_NOT_AVAILABLE, JaxError.NULL_CUSTOMER_ID, JaxError.INVALID_CUSTOMER })
	AmxApiResponse<String, Object> saveCustomeKycDocument(ImageSubmissionRequest modelData) throws ParseException;

	@ApiJaxStatus({ JaxError.SIGNATURE_NOT_AVAILABLE, JaxError.NULL_CUSTOMER_ID, JaxError.INVALID_CUSTOMER })
	AmxApiResponse<String, Object> saveCustomerSignature(ImageSubmissionRequest model);

	@ApiJaxStatus({ JaxError.ALREADY_EXIST_EMAIL, JaxError.INVALID_MOBILE_NUMBER })
	AmxApiResponse<SendOtpModel, Object> sendOtp(CustomerPersonalDetail customerPersonalDetail);
	
	AmxApiResponse<CustomerCredential, Object> saveLoginDetailOffsite(CustomerCredential customerCredential);
	
	AmxApiResponse<OffsiteCustomerDataDTO, Object> getOffsiteCustomerData(String identityInt, BigDecimal identityType);

}
