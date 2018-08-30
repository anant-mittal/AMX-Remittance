package com.amx.jax.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.ICustRegService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.model.request.CommonRequest;
import com.amx.jax.model.request.DynamicFieldRequest;
import com.amx.jax.model.request.EmploymentDetailsRequest;
import com.amx.jax.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.model.response.ArticleDetailsDescDto;
import com.amx.jax.model.response.ArticleMasterDescDto;
import com.amx.jax.model.response.FieldListDto;
import com.amx.jax.model.response.IncomeRangeDto;
import com.amx.jax.rest.RestService;

@Component
public class CustRegClient implements ICustRegService {

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	public AmxApiResponse<Map<String, FieldListDto>, Object> getFieldList(DynamicFieldRequest model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<IncomeRangeDto, Object> getIncomeRangeResponse(EmploymentDetailsRequest model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<ArticleDetailsDescDto, Object> getDesignationListResponse(EmploymentDetailsRequest model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<ArticleMasterDescDto, Object> getArticleListResponse(CommonRequest model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<String, Object> validateOtpForEmailAndMobile(
			OffsiteCustomerRegistrationRequest offsiteCustRegModel) {
		// TODO Auto-generated method stub
		return null;
	}

}
