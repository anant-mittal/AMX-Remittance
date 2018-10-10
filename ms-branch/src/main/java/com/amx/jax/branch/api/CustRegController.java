package com.amx.jax.branch.api;

import java.text.ParseException;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.ICustRegService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branch.service.OffsitCustRegService;
import com.amx.jax.constants.JaxEvent;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
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
import com.amx.jax.service.CountryService;
import com.amx.jax.service.MetaService;
import com.amx.jax.service.ViewDistrictService;
import com.amx.jax.service.ViewStateService;
import com.amx.jax.utils.JaxContextUtil;

@RestController
public class CustRegController implements ICustRegService {

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
	ViewDistrictService districtService;

	@Autowired
	MetaService metaService;

	@RequestMapping(value = CustRegApiEndPoints.GET_ID_TYPES, method = RequestMethod.POST)
	public AmxApiResponse<ComponentDataDto, Object> getIdTypes() {
		return offsiteCustRegService.getIdTypes();
	}

	@RequestMapping(value = CustRegApiEndPoints.GET_CUSTOMER_OTP, method = RequestMethod.POST)
	public AmxApiResponse<SendOtpModel, Object> sendOtp(@RequestBody CustomerPersonalDetail customerPersonalDetail) {
		JaxContextUtil.setJaxEvent(JaxEvent.MOBILE_EMAIL_OTP);
		JaxContextUtil.setRequestModel(customerPersonalDetail);
		LOGGER.info("send otp request: " + customerPersonalDetail);
		// return
		// AmxApiResponse.build(customerRegistrationService.sendOtp(customerPersonalDetail).getResults());
		return offsiteCustRegService.sendOtp(customerPersonalDetail);

	}

	@RequestMapping(value = CustRegApiEndPoints.VALIDATE_OTP, method = RequestMethod.POST)
	public AmxApiResponse<String, Object> validateOtpForEmailAndMobile(
			@RequestBody OffsiteCustomerRegistrationRequest offsiteCustRegModel) {
		return offsiteCustRegService.validateOtpForEmailAndMobile(offsiteCustRegModel);
	}

	@RequestMapping(value = CustRegApiEndPoints.GET_ARTICLE_LIST, method = RequestMethod.POST)
	public AmxApiResponse<ArticleMasterDescDto, Object> getArticleListResponse() {
		return offsiteCustRegService.getArticleListResponse();
	}

	@RequestMapping(value = CustRegApiEndPoints.GET_DESIGNATION_LIST, method = RequestMethod.POST)
	public AmxApiResponse<ArticleDetailsDescDto, Object> getDesignationListResponse(
			@RequestBody EmploymentDetailsRequest model) {
		return offsiteCustRegService.getDesignationListResponse(model);
	}

	@RequestMapping(value = CustRegApiEndPoints.GET_INCOME_RANGE_LIST, method = RequestMethod.POST)
	public AmxApiResponse<IncomeRangeDto, Object> getIncomeRangeResponse(@RequestBody EmploymentDetailsRequest model) {
		return offsiteCustRegService.getIncomeRangeResponse(model);
	}

	@RequestMapping(value = CustRegApiEndPoints.GET_DYNAMIC_FIELDS, method = RequestMethod.POST)
	public AmxApiResponse<Map<String, FieldListDto>, Object> getFieldList(@RequestBody DynamicFieldRequest model) {
		return offsiteCustRegService.getFieldList(model);
	}

	@RequestMapping(value = CustRegApiEndPoints.GET_EMPLOYMENT_TYPE_LIST, method = RequestMethod.POST)
	public AmxApiResponse<ComponentDataDto, Object> sendEmploymentTypeList() {
		return offsiteCustRegService.sendEmploymentTypeList();
	}

	@RequestMapping(value = CustRegApiEndPoints.GET_PROFESSION_LIST, method = RequestMethod.POST)
	public AmxApiResponse<ComponentDataDto, Object> sendProfessionList() {
		return offsiteCustRegService.sendProfessionList();
	}

	@RequestMapping(value = CustRegApiEndPoints.SAVE_CUST_INFO, method = RequestMethod.POST)
	public AmxApiResponse<CustomerInfo, Object> saveCustomerInfo(@RequestBody CustomerInfoRequest model) {
		return offsiteCustRegService.saveCustomerInfo(model);
	}

	@RequestMapping(value = CustRegApiEndPoints.SAVE_KYC_DOC, method = RequestMethod.POST)
	public AmxApiResponse<String, Object> saveCustomeKycDocument(@RequestBody ImageSubmissionRequest model)
			throws ParseException {
		return offsiteCustRegService.saveCustomeKycDocument(model);
	}

	@RequestMapping(value = CustRegApiEndPoints.SAVE_SIGNATURE, method = RequestMethod.POST)
	public AmxApiResponse<String, Object> saveCustomerSignature(@RequestBody ImageSubmissionRequest model) {
		return offsiteCustRegService.saveCustomerSignature(model);
	}

	@RequestMapping(value = CustRegApiEndPoints.SCAN_CARD, method = RequestMethod.POST)
	public AmxApiResponse<CardDetail, Object> cardScan(CardDetail cardDetail) {
		return offsiteCustRegService.cardScan(cardDetail);
	}

}
