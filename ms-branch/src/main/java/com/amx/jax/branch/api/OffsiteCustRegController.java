package com.amx.jax.branch.api;

import static com.amx.amxlib.constant.ApiEndpoint.OFFSITE_CUSTOMER_REG;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.CustomerPersonalDetail;
import com.amx.jax.ICustRegService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branch.service.OffsitCustRegService;
import com.amx.jax.constants.JaxEvent;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.error.JaxError;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.CustomerInfoRequest;
import com.amx.jax.model.request.DynamicFieldRequest;
import com.amx.jax.model.request.EmploymentDetailsRequest;
import com.amx.jax.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.model.response.ArticleDetailsDescDto;
import com.amx.jax.model.response.ArticleMasterDescDto;
import com.amx.jax.model.response.ComponentDataDto;
import com.amx.jax.model.response.FieldListDto;
import com.amx.jax.model.response.IncomeRangeDto;
import com.amx.jax.service.CountryService;
import com.amx.jax.service.MetaService;
import com.amx.jax.service.ViewDistrictService;
import com.amx.jax.service.ViewStateService;
import com.amx.jax.userservice.service.CustomerRegistrationService;
import com.amx.jax.utils.JaxContextUtil;

@RestController
@RequestMapping(OFFSITE_CUSTOMER_REG)
public class OffsiteCustRegController implements ICustRegService {

	private static final Logger LOGGER = LoggerService.getLogger(OffsitCustRegService.class);

	@Autowired
	OffsitCustRegService offsiteCustRegService;

	@Autowired
	CountryService countryService;

	@Autowired
	MetaData metaData;

	@Autowired
	ViewStateService stateService;

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	@Autowired
	ViewDistrictService districtService;

	@Autowired
	MetaService metaService;	

	@ApiJaxStatus({JaxError.EMPTY_ID_TYPE_LIST})
	@RequestMapping(value = CustRegApiEndPoints.GET_ID_TYPES, method = RequestMethod.POST)
	public AmxApiResponse<ComponentDataDto, Object> sendIdTypes() {
		return offsiteCustRegService.sendIdTypes();
	}

	@RequestMapping(value = CustRegApiEndPoints.GET_CUSTOMER_OTP, method = RequestMethod.POST)
	public AmxApiResponse<List, Object> sendOtpForEmailAndMobile(
			@RequestBody CustomerPersonalDetail customerPersonalDetail) {
		JaxContextUtil.setJaxEvent(JaxEvent.MOBILE_EMAIL_OTP);
		JaxContextUtil.setRequestModel(customerPersonalDetail);
		LOGGER.info("send otp request: " + customerPersonalDetail);
		return AmxApiResponse.build(customerRegistrationService.sendOtp(customerPersonalDetail).getResults());
	}

	@ApiJaxStatus({JaxError.MISSING_OTP,JaxError.VALIDATE_OTP_LIMIT_EXCEEDED})
	@RequestMapping(value = CustRegApiEndPoints.VALIDATE_OTP, method = RequestMethod.POST)
	public AmxApiResponse<String, Object> validateOtpForEmailAndMobile(
			@RequestBody OffsiteCustomerRegistrationRequest offsiteCustRegModel) {	
		return offsiteCustRegService.validateOtpForEmailAndMobile(offsiteCustRegModel);
	}
	
	@ApiJaxStatus({JaxError.EMPTY_ARTICLE_LIST})
	@RequestMapping(value = CustRegApiEndPoints.GET_ARTICLE_LIST, method = RequestMethod.POST)
	public AmxApiResponse<ArticleMasterDescDto, Object> getArticleListResponse() {		
		return offsiteCustRegService.getArticleListResponse();
	}

	@ApiJaxStatus({JaxError.EMPTY_DESIGNATION_LIST})
	@RequestMapping(value = CustRegApiEndPoints.GET_DESIGNATION_LIST, method = RequestMethod.POST)
	public AmxApiResponse<ArticleDetailsDescDto, Object> getDesignationListResponse(
			@RequestBody EmploymentDetailsRequest model) {		
		return offsiteCustRegService.getDesignationListResponse(model);
	}

	@ApiJaxStatus({JaxError.EMPTY_INCOME_RANGE})
	@RequestMapping(value = CustRegApiEndPoints.GET_INCOME_RANGE_LIST, method = RequestMethod.POST)
	public AmxApiResponse<IncomeRangeDto, Object> getIncomeRangeResponse(@RequestBody EmploymentDetailsRequest model) {		
		return offsiteCustRegService.getIncomeRangeResponse(model);
	}

	@ApiJaxStatus({JaxError.EMPTY_FIELD_CONDITION})
	@RequestMapping(value = CustRegApiEndPoints.GET_DYNAMIC_FIELDS, method = RequestMethod.POST)
	public AmxApiResponse<Map<String, FieldListDto>, Object> getFieldList(@RequestBody DynamicFieldRequest model) {		
		return offsiteCustRegService.getFieldList(model);
	}
	
	@ApiJaxStatus({JaxError.EMPTY_EMPLOYMENT_TYPE})
	@RequestMapping(value = CustRegApiEndPoints.GET_EMPLOYMENT_TYPE_LIST, method = RequestMethod.POST)
	public AmxApiResponse<ComponentDataDto, Object> sendEmploymentTypeList() {		
		return offsiteCustRegService.sendEmploymentTypeList();
	}
	
	@ApiJaxStatus({JaxError.EMPTY_PROFESSION_LIST})
	@RequestMapping(value = CustRegApiEndPoints.GET_PROFESSION_LIST, method = RequestMethod.POST)
	public AmxApiResponse<ComponentDataDto, Object> sendProfessionList() {		
		return offsiteCustRegService.sendProfessionList();
	}

	@ApiJaxStatus({JaxError.EXISTING_CIVIL_ID})
	@RequestMapping(value = CustRegApiEndPoints.SAVE_CUST_INFO, method = RequestMethod.POST)
	public AmxApiResponse<BigDecimal, Object> saveCustomerInfo(@RequestBody CustomerInfoRequest model) {		
		return offsiteCustRegService.saveCustomerInfo(model);
	}

}
