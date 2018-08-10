package com.amx.jax.branch.api;

import static com.amx.amxlib.constant.ApiEndpoint.OFFSITE_CUSTOMER_REG;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.ArticleDetailsDescDto;
import com.amx.amxlib.meta.model.ArticleMasterDescDto;
import com.amx.amxlib.meta.model.IncomeRangeDto;
import com.amx.amxlib.meta.model.ViewCityDto;
import com.amx.amxlib.meta.model.ViewDistrictDto;
import com.amx.amxlib.meta.model.ViewStateDto;
import com.amx.amxlib.model.BizComponentDataDescDto;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerPersonalDetail;
import com.amx.amxlib.model.request.CommonRequest;
import com.amx.amxlib.model.request.DynamicFieldRequest;
import com.amx.amxlib.model.request.EmploymentDetailsRequest;
import com.amx.amxlib.model.request.GetJaxFieldRequest;
import com.amx.amxlib.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branch.service.OffsitCustRegService;
import com.amx.jax.constants.JaxEvent;
import com.amx.jax.dbmodel.ArticleMasterDesc;
import com.amx.jax.dbmodel.CountryMasterView;
import com.amx.jax.dbmodel.FieldList;
import com.amx.jax.dbmodel.JaxConditionalFieldRuleDto;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.service.CountryService;
import com.amx.jax.service.MetaService;
import com.amx.jax.service.ViewDistrictService;
import com.amx.jax.service.ViewStateService;
import com.amx.jax.userservice.service.CustomerRegistrationService;
import com.amx.jax.utils.JaxContextUtil;

@RestController
@RequestMapping(OFFSITE_CUSTOMER_REG)
public class OffsiteCustRegController /*implements ICustRegService*/ {
	
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
	
	/*@Override
	@RequestMapping(value = CustRegApiEndPoints.GET_MODES, method = RequestMethod.GET)
	public AmxApiResponse<BigDecimal, Object> getModes() {
		return offsiteCustRegService.getModes();
	}*/
	
	@RequestMapping(value ="/get-field-list" , method = RequestMethod.POST)
	public AmxApiResponse<List<JaxConditionalFieldRuleDto>, Object> getIdDetailsFields(@RequestBody GetJaxFieldRequest model) {
		JaxContextUtil.setJaxEvent(JaxEvent.FIELD_LIST);
		JaxContextUtil.setRequestModel(model);
		return offsiteCustRegService.getIdDetailsFields(model);
	}
	
	@RequestMapping(value = "/country", method = RequestMethod.GET)
	public AmxApiResponse<List<CountryMasterView>,Object> getCountryListResponse() {
		JaxContextUtil.setJaxEvent(JaxEvent.COUNTRY_LIST);
		//JaxContextUtil.setRequestModel();
		return countryService.getCountryListOffsite();
	}	
	
	@RequestMapping(value = "/employee-send-otp", method = RequestMethod.POST)
	public AmxApiResponse<CivilIdOtpModel, Object> validateEmployeeDetails(@RequestBody OffsiteCustomerRegistrationRequest offsiteCustRegModel) {
		JaxContextUtil.setJaxEvent(JaxEvent.SEND_OTP);
		JaxContextUtil.setRequestModel(offsiteCustRegModel);
		return  offsiteCustRegService.validateEmployeeDetails(offsiteCustRegModel);
	}	
	
	@RequestMapping(value = "/employee-validate-otp", method = RequestMethod.POST)
	public AmxApiResponse<String, Object> validateOTP(@RequestBody OffsiteCustomerRegistrationRequest offsiteCustRegModel) {
		JaxContextUtil.setJaxEvent(JaxEvent.VALIDATE_OTP);
		JaxContextUtil.setRequestModel(offsiteCustRegModel);
		return  offsiteCustRegService.validateOTP(offsiteCustRegModel);
	}
	
	@RequestMapping(value = "/send-id-types", method = RequestMethod.POST)
	public AmxApiResponse<List<BizComponentDataDescDto>, Object> sendIdTypes() {
		JaxContextUtil.setJaxEvent(JaxEvent.ID_TYPE);
		//JaxContextUtil.setRequestModel();
		return  offsiteCustRegService.sendIdTypes();
	}	
	
	@RequestMapping(value = "/state", method = RequestMethod.POST)
	public AmxApiResponse<List<ViewStateDto>, Object> getStateList(@RequestBody CommonRequest model) {
		JaxContextUtil.setJaxEvent(JaxEvent.STATE_LIST);
		JaxContextUtil.setRequestModel(model);
		return  stateService.getStateListOffsite(model.getCountryId(), metaData.getLanguageId());
	}	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/customer-mobile-email-send-otp", method = RequestMethod.POST)
	public AmxApiResponse<List, Object> sendOtpForEmailAndMobile(@RequestBody CustomerPersonalDetail customerPersonalDetail) {
		JaxContextUtil.setJaxEvent(JaxEvent.MOBILE_EMAIL_OTP);
		JaxContextUtil.setRequestModel(customerPersonalDetail);	
		LOGGER.info("send otp request: " + customerPersonalDetail);
		return  AmxApiResponse.build(customerRegistrationService.sendOtp(customerPersonalDetail).getResults());
	}	
	
	@RequestMapping(value = "/customer-mobile-email-validate-otp", method = RequestMethod.POST)
	public AmxApiResponse<String, Object> validateOtpForEmailAndMobile(@RequestBody OffsiteCustomerRegistrationRequest offsiteCustRegModel) {
		JaxContextUtil.setJaxEvent(JaxEvent.VALIDATE_OTP);
		JaxContextUtil.setRequestModel(offsiteCustRegModel);
		return  offsiteCustRegService.validateOtp(offsiteCustRegModel);
	}	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/districtlist", method = RequestMethod.POST)
	public AmxApiResponse<List<ViewDistrictDto>, Object> getDistrictNameResponse(@RequestBody CommonRequest model){		
		JaxContextUtil.setJaxEvent(JaxEvent.DISTRICT_LIST);
		JaxContextUtil.setRequestModel(model);
		return AmxApiResponse.build(districtService.getAllDistrict(model.getStateId(),metaData.getLanguageId()).getResults());
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/citylist", method = RequestMethod.POST)
	public AmxApiResponse<List<ViewCityDto>, Object> getCityListResponse(@RequestBody CommonRequest model){	
		JaxContextUtil.setJaxEvent(JaxEvent.CITY_LIST);
		JaxContextUtil.setRequestModel(model);
		return AmxApiResponse.build(metaService.getDistrictCity(model.getDistrictId(),metaData.getLanguageId()).getResults());
	}	
	
	@RequestMapping(value = "/articleList", method = RequestMethod.POST)
	public AmxApiResponse<List<ArticleMasterDescDto>, Object> getArticleListResponse(@RequestBody CommonRequest model){	
		//JaxContextUtil.setJaxEvent(JaxEvent.CITY_LIST);
		//JaxContextUtil.setRequestModel(model);
		return offsiteCustRegService.getArticleListResponse(model.getCountryId(),metaData.getLanguageId());
	}
	
	@RequestMapping(value = "/designationList", method = RequestMethod.POST)
	public AmxApiResponse<List<ArticleDetailsDescDto>, Object> getDesignationListResponse(@RequestBody EmploymentDetailsRequest model){	
		//JaxContextUtil.setJaxEvent(JaxEvent.CITY_LIST);
		//JaxContextUtil.setRequestModel(model);
		return offsiteCustRegService.getDesignationListResponse(model.getArticleId(),metaData.getLanguageId());
	}
	
	@RequestMapping(value = "/incomeRangeList", method = RequestMethod.POST)
	public AmxApiResponse<List<IncomeRangeDto>, Object> getIncomeRangeResponse(@RequestBody EmploymentDetailsRequest model){	
		//JaxContextUtil.setJaxEvent(JaxEvent.CITY_LIST);
		//JaxContextUtil.setRequestModel(model);
		return offsiteCustRegService.getIncomeRangeResponse(metaData.getCountryId(),model.getArticleDetailsId());
	}
	
	@RequestMapping(value ="/new-field-list" , method = RequestMethod.POST)
	public AmxApiResponse<List<FieldList>, Object> getFieldList(@RequestBody DynamicFieldRequest model) {
		//JaxContextUtil.setJaxEvent(JaxEvent.FIELD_LIST);
		//JaxContextUtil.setRequestModel(model);
		return offsiteCustRegService.getFieldList(model);
	}
}
