package com.amx.jax.customer.api;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.CustomerCredential;
import com.amx.jax.ICustRegService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.customer.manager.OffsiteAddressProofManager;
import com.amx.jax.customer.service.OffsitCustRegService;
import com.amx.jax.error.JaxError;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.CardDetail;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.dto.SendOtpModel;
import com.amx.jax.model.request.CustomerInfoRequest;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.DynamicFieldRequest;
import com.amx.jax.model.request.EmploymentDetailsRequest;
import com.amx.jax.model.request.ImageSubmissionRequest;
import com.amx.jax.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.model.request.customer.GetOffsiteCustomerDetailRequest;
import com.amx.jax.model.response.ArticleDetailsDescDto;
import com.amx.jax.model.response.ArticleMasterDescDto;
import com.amx.jax.model.response.ComponentDataDto;
import com.amx.jax.model.response.CustomerInfo;
import com.amx.jax.model.response.FieldListDto;
import com.amx.jax.model.response.IncomeRangeDto;
import com.amx.jax.model.response.customer.AddressProofDTO;
import com.amx.jax.model.response.customer.OffsiteCustomerDataDTO;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.jax.service.CountryService;
import com.amx.jax.service.MetaService;
import com.amx.jax.service.ViewDistrictService;
import com.amx.jax.service.ViewStateService;
import com.amx.jax.userservice.service.CustomerRegistrationService;
import com.amx.jax.userservice.service.UserService;
import com.jax.amxlib.exception.jax.GlobaLException;

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
	private CustomerRegistrationService customerRegistrationService;

	@Autowired
	ViewDistrictService districtService;

	@Autowired
	MetaService metaService;

	@Autowired
	OffsiteAddressProofManager offsiteAddressProofManager;
	
	@Autowired
	UserService userService;

	@RequestMapping(value = CustRegApiEndPoints.GET_ID_TYPES, method = RequestMethod.POST)
	public AmxApiResponse<ComponentDataDto, Object> getIdTypes() {
		return offsiteCustRegService.getIdTypes();
	}

	@Override
	@RequestMapping(value = CustRegApiEndPoints.GET_CUSTOMER_OTP, method = RequestMethod.POST)
	public AmxApiResponse<SendOtpModel, Object> sendOtp(
			@RequestBody @Valid CustomerPersonalDetail customerPersonalDetail) {
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
	public AmxApiResponse<CustomerInfo, Object> saveCustomerInfo(@RequestBody @Valid CustomerInfoRequest model) {
		return offsiteCustRegService.saveCustomerInfo(model);
	}

	@RequestMapping(value = CustRegApiEndPoints.SAVE_KYC_DOC, method = RequestMethod.POST)
	public AmxApiResponse<String, Object> saveCustomeKycDocument(@RequestBody ImageSubmissionRequest model)
			throws ParseException {
		return offsiteCustRegService.saveCustomeKycDocumentAndPopulateCusmas(model);
	}

	@RequestMapping(value = CustRegApiEndPoints.SAVE_SIGNATURE, method = RequestMethod.POST)
	public AmxApiResponse<String, Object> saveCustomerSignature(@RequestBody ImageSubmissionRequest model) {
		return offsiteCustRegService.saveCustomerSignature(model);
	}

	@RequestMapping(value = CustRegApiEndPoints.SCAN_CARD, method = RequestMethod.POST)
	public AmxApiResponse<CardDetail, Object> cardScan(CardDetail cardDetail) {
		return offsiteCustRegService.cardScan(cardDetail);
	}

	@RequestMapping(value = CustRegApiEndPoints.SAVE_OFFSITE_LOGIN, method = RequestMethod.POST)
	public AmxApiResponse<CustomerCredential, Object> saveLoginDetailOffsite(
			@RequestBody CustomerCredential customerCredential) {
		return offsiteCustRegService.saveLoginDetailOffsite(customerCredential);
	}

	@RequestMapping(value = CustRegApiEndPoints.GET_OFFSITE_CUSTOMER_DATA, method = RequestMethod.GET)
	public AmxApiResponse<OffsiteCustomerDataDTO, Object> getOffsiteCustomerData(
			@RequestParam(value = "identityInt", required = true) String identityInt,
			@RequestParam(value = "identityType", required = true) BigDecimal identityType) {
		return offsiteCustRegService.getOffsiteCustomerData(identityInt, identityType);
	}

	@RequestMapping(value = CustRegApiEndPoints.DESIGNATION_LIST, method = RequestMethod.GET)
	public AmxApiResponse<ResourceDTO, Object> getDesignationList() {
		return offsiteCustRegService.getDesignationList();
	}

	@RequestMapping(value = CustRegApiEndPoints.GET_CUSTOMER_DEATILS, method = RequestMethod.GET)
	public AmxApiResponse<OffsiteCustomerDataDTO, Object> getOffsiteCustomerDetails(
			@RequestParam(value = "identityInt", required = false) String identityInt,
		@RequestParam(value = "identityType", required = true) BigDecimal identityType,
		@RequestParam(value = "customerId", required = false) BigDecimal customerId) {
		
		if(identityInt == null && customerId == null)
		{
			throw new GlobalException(JaxError.VALIDATION_NOT_NULL, "Field should not be null");

		}
		AmxApiResponse<OffsiteCustomerDataDTO, Object> response=null;
		if(identityInt == null)
		{
			PersonInfo personInfo = userService.getPersonInfo(customerId);
			String identityIntByCustId =personInfo.getIdentityInt();
			response=offsiteCustRegService.getOffsiteCustomerDetails(identityIntByCustId, identityType,customerId);
		}
		else {
		response=offsiteCustRegService.getOffsiteCustomerDetails(identityInt, identityType,customerId);
		}

		return response;
	}

	@RequestMapping(value = CustRegApiEndPoints.GET_OFFSITE_CUSTOMER_DATA_V1, method = RequestMethod.GET)
	public AmxApiResponse<OffsiteCustomerDataDTO, Object> getOffsiteCustomerDataV1(
			@RequestBody @Valid GetOffsiteCustomerDetailRequest request) {
		return offsiteCustRegService.getOffsiteCustomerData(request);
	}

	@RequestMapping(value = CustRegApiEndPoints.ADDRESS_PROOF, method = RequestMethod.GET)
	public AmxApiResponse<AddressProofDTO, Object> getAddressProof() {
		return offsiteAddressProofManager.getAddressProof();
	}

	@RequestMapping(value = CustRegApiEndPoints.DOCUMENT_UPLOAD_REFERENCE, method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> saveDocumentUploadReference(
			@RequestBody ImageSubmissionRequest imageSubmissionRequest) throws Exception {
		return AmxApiResponse.build(offsiteAddressProofManager.saveDocumentUploadReference(imageSubmissionRequest));
	}

	
	

}
