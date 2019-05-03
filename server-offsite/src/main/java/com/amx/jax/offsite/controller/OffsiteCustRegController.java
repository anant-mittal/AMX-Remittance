package com.amx.jax.offsite.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.CustomerCredential;
import com.amx.jax.ICustRegService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.api.ListRequestModel;
import com.amx.jax.branch.common.OffsiteStatus.ApiOffisteStatus;
import com.amx.jax.branch.common.OffsiteStatus.OffsiteServerCodes;
import com.amx.jax.client.CustomerRegistrationClient;
import com.amx.jax.client.MetaClient;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.error.JaxError;
import com.amx.jax.http.CommonHttpRequest.CommonMediaType;
import com.amx.jax.model.CardDetail;
import com.amx.jax.model.auth.QuestModelDTO;
import com.amx.jax.model.customer.SecurityQuestionModel;
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
import com.amx.jax.offsite.service.CustomerSession;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.utils.ArgUtil;

/**
 * 
 * @author lalittanwar
 *
 */
@RestController
@RequestMapping(value = "/api/customer/reg", produces = { CommonMediaType.APPLICATION_JSON_VALUE,
		CommonMediaType.APPLICATION_V0_JSON_VALUE })
@ApiStatusService(ICustRegService.class)
public class OffsiteCustRegController {

	private Logger logger = Logger.getLogger(OffsiteCustRegController.class);

	@Autowired
	private ICustRegService offsiteCustRegClient;

	@Autowired
	private CustomerSession customerSession;

	@Autowired
	private MetaClient metaClient;

	@Autowired
	CustomerRegistrationClient customerRegistrationClient;

	@RequestMapping(value = "/id_type/list", method = { RequestMethod.POST })
	public AmxApiResponse<ComponentDataDto, Object> getIdTypes() {
		return offsiteCustRegClient.getIdTypes();
	}

	@RequestMapping(value = "/dynamic_field/list", method = { RequestMethod.POST })
	public AmxApiResponse<Map<String, FieldListDto>, Object> getFieldList(@RequestBody DynamicFieldRequest model) {
		customerSession.clear();
		return offsiteCustRegClient.getFieldList(model);
	}

	@RequestMapping(value = "/professions/list", method = { RequestMethod.GET })
	public AmxApiResponse<ComponentDataDto, Object> sendProfessionList() {
		return offsiteCustRegClient.sendProfessionList();
	}

	@RequestMapping(value = "/article/list", method = { RequestMethod.POST })
	public AmxApiResponse<ArticleMasterDescDto, Object> getArticleListResponse() {
		return offsiteCustRegClient.getArticleListResponse();
	}

	@RequestMapping(value = "/designation/list", method = { RequestMethod.POST })
	public AmxApiResponse<ArticleDetailsDescDto, Object> getDesignationListResponse(
			@RequestBody EmploymentDetailsRequest model) {
		return offsiteCustRegClient.getDesignationListResponse(model);
	}

	@RequestMapping(value = "/income_range/list", method = { RequestMethod.POST })
	public AmxApiResponse<IncomeRangeDto, Object> getIncomeRangeResponse(@RequestBody EmploymentDetailsRequest model) {
		return offsiteCustRegClient.getIncomeRangeResponse(model);
	}

	@RequestMapping(value = "/employment_type/list", method = { RequestMethod.POST })
	public AmxApiResponse<ComponentDataDto, Object> sendEmploymentTypeList() {
		return offsiteCustRegClient.sendEmploymentTypeList();
	}

	@RequestMapping(value = "/card/read", method = { RequestMethod.POST })
	public AmxApiResponse<CardDetail, Object> cardScan(@RequestBody CardDetail cardDetail) {
		return offsiteCustRegClient.cardScan(cardDetail);
	}

	@RequestMapping(value = "/customer_info/get", method = { RequestMethod.GET })
	public AmxApiResponse<OffsiteCustomerDataDTO, Object> getOffsiteCustomerData(String identity,
			BigDecimal identityType) {
		AmxApiResponse<OffsiteCustomerDataDTO, Object> x = offsiteCustRegClient.getOffsiteCustomerData(identity,
				identityType);
		if (!ArgUtil.isEmpty(x.getResult())
				&& !ArgUtil.isEmpty(x.getResult().getCustomerPersonalDetail())) {
			customerSession.setCustomerId(x.getResult().getCustomerPersonalDetail().getCustomerId());
		}
		return x;
	}

	@RequestMapping(value = "/customer_info/save", method = { RequestMethod.POST })
	public AmxApiResponse<CustomerInfo, Object> saveCustomerInfo(@RequestBody CustomerInfoRequest model) {
		AmxApiResponse<CustomerInfo, Object> info = offsiteCustRegClient.saveCustomerInfo(model);
		customerSession.setCustomerId(info.getResult().getCustomerId());
		return info;
	}

	@RequestMapping(value = "/kycdoc/submit", method = { RequestMethod.POST })
	public AmxApiResponse<String, Object> saveCustomeKycDocument(@RequestBody ImageSubmissionRequest modelData)
			throws ParseException {
		return offsiteCustRegClient.saveCustomeKycDocument(modelData);
	}

	@RequestMapping(value = "/signature/submit", method = { RequestMethod.POST })
	public AmxApiResponse<String, Object> saveCustomerSignature(@RequestBody ImageSubmissionRequest modelData)
			throws ParseException {
		return offsiteCustRegClient.saveCustomerSignature(modelData);
	}

	@RequestMapping(value = "/secques/list", method = { RequestMethod.GET })
	public AmxApiResponse<QuestModelDTO, Object> getSequrityQuestion() {
		return metaClient.getSequrityQuestion();
	}

	@RequestMapping(value = "/secques/set", method = { RequestMethod.POST })
	public AmxApiResponse<SecurityQuestionModel, Object> setSecQues(
			@RequestBody ListRequestModel<SecurityQuestionModel> req) {
		customerRegistrationClient.saveSecurityQuestions(req.getValues());
		return AmxApiResponse.buildList(req.getValues());
	}

	@RequestMapping(value = "/phising/set", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> savePhishiingImage(@RequestParam String imageUrl,
			@RequestParam String caption) {
		return AmxApiResponse.build(customerRegistrationClient.savePhishiingImage(caption, imageUrl).getResult());
	}

	@RequestMapping(value = "/creds/set", method = { RequestMethod.POST })
	public AmxApiResponse<CustomerCredential, Object> saveLoginDetail(@RequestBody CustomerCredential req) {
		return AmxApiResponse.build(offsiteCustRegClient.saveLoginDetailOffsite(req).getResult());
	}

	@ApiOffisteStatus({ OffsiteServerCodes.DOTP_REQUIRED })
	@ApiJaxStatus({ JaxError.MISSING_OTP, JaxError.VALIDATE_OTP_LIMIT_EXCEEDED })
	@RequestMapping(value = "/personal/save", method = { RequestMethod.POST })
	public AmxApiResponse<OffsiteCustomerRegistrationRequest, SendOtpModel> sendOtpForEmailAndMobile(

			@RequestHeader(value = "mOtp", required = false) String mOtpHeader,
			@RequestHeader(value = "eOtp", required = false) String eOtpHeader,
			@RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp,

			@RequestBody CustomerPersonalDetail customerPersonalDetail) {
		mOtp = ArgUtil.ifNotEmpty(mOtp, mOtpHeader);
		eOtp = ArgUtil.ifNotEmpty(eOtp, eOtpHeader);
		SendOtpModel otpmodel = null;
		OffsiteCustomerRegistrationRequest req = null;
		customerSession.setTranxId(customerPersonalDetail.getIdentityInt());
		AmxApiResponse<OffsiteCustomerRegistrationRequest, SendOtpModel> resp = new AmxApiResponse<OffsiteCustomerRegistrationRequest, SendOtpModel>();
		if (mOtp == null) {
			otpmodel = offsiteCustRegClient.sendOtp(customerPersonalDetail).getResult();
			resp.setMeta(otpmodel);
			if (ArgUtil.isEmpty(customerPersonalDetail.getEmail())) {
				resp.setStatusEnum(OffsiteServerCodes.MOTP_REQUIRED);
			} else {
				resp.setStatusEnum(OffsiteServerCodes.DOTP_REQUIRED);
			}
		} else {
			OffsiteCustomerRegistrationRequest offsiteCustRegModel = new OffsiteCustomerRegistrationRequest();
			offsiteCustRegModel.seteOtp(eOtp);
			offsiteCustRegModel.setmOtp(mOtp);
			offsiteCustRegClient.validateOtpForEmailAndMobile(offsiteCustRegModel);
		}
		return resp;
	}

}
