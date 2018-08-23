package com.amx.jax;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import com.amx.jax.api.ARespModel;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.model.request.CommonRequest;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.DynamicFieldRequest;
import com.amx.jax.model.request.EmploymentDetailsRequest;
import com.amx.jax.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.model.response.ArticleDetailsDescDto;
import com.amx.jax.model.response.ArticleMasterDescDto;
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
	}

	@JsonDeserialize(as = CustRegRequestModel.class)
	public interface RegModeModel {
		public BigDecimal getMode();

		public void setMode(BigDecimal modeId);
	}

	/**
	 * Get List of modes, available for customer registration
	 * 
	 * @return
	 */
	AmxApiResponse<BigDecimal, Object> getModes();
	
	/**
	 * Get List of fields required for identity validation, for particular mode id
	 * 
	 * @return
	 */
	AmxApiResponse<ARespModel, Object> getIdDetailsFields(RegModeModel modeId);
	
	AmxApiResponse<Map<String, FieldListDto>, Object> getFieldList(DynamicFieldRequest model);
	
	AmxApiResponse<IncomeRangeDto, Object> getIncomeRangeResponse(EmploymentDetailsRequest model);
	
	AmxApiResponse<ArticleDetailsDescDto, Object> getDesignationListResponse(EmploymentDetailsRequest model);
	
	AmxApiResponse<ArticleMasterDescDto, Object> getArticleListResponse(CommonRequest model);
	
	AmxApiResponse<String, Object> validateOtpForEmailAndMobile(OffsiteCustomerRegistrationRequest offsiteCustRegModel);
	
	AmxApiResponse<List, Object> sendOtpForEmailAndMobile(CustomerPersonalDetail customerPersonalDetail);

}
