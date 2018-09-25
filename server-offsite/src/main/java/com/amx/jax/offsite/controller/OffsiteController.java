package com.amx.jax.offsite.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.CustomerCredential;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.api.ListRequestModel;
import com.amx.jax.client.CustomerRegistrationClient;
import com.amx.jax.client.MetaClient;
import com.amx.jax.client.OffsiteCustRegClient;
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
import com.amx.jax.model.response.FieldListDto;
import com.amx.jax.model.response.IncomeRangeDto;
import com.amx.jax.offsite.service.OffsiteService;

/**
 * 
 * @author lalittanwar
 *
 */
@RestController
@RequestMapping("/api/customer/reg")
public class OffsiteController {

	private Logger logger = Logger.getLogger(OffsiteController.class);

	@Autowired
	private OffsiteCustRegClient offsiteCustRegClient;

	@Autowired
	private OffsiteService offsiteService;

	@Autowired
	private MetaClient metaClient;

	@RequestMapping(value = "/id_type/list", method = { RequestMethod.POST })
	public AmxApiResponse<ComponentDataDto, Object> getIdTypes() {
		return offsiteCustRegClient.sendIdTypes();
	}

	@RequestMapping(value = "/dynamic_field/list", method = { RequestMethod.POST })
	public AmxApiResponse<FieldListDto, Object> getFieldList(@RequestBody DynamicFieldRequest model) {
		return offsiteService.getFieldList(model);
	}

	@RequestMapping(value = "/professions/list", method = { RequestMethod.GET })
	public AmxApiResponse<ComponentDataDto, Object> getProfessionList() {
		return offsiteCustRegClient.sendProfessionList();
	}

	@RequestMapping(value = "/article/list", method = { RequestMethod.POST })
	public AmxApiResponse<ArticleMasterDescDto, Object> getArticleList() {
		return offsiteCustRegClient.getArticleListResponse();
	}

	@RequestMapping(value = "/designation/list", method = { RequestMethod.POST })
	public AmxApiResponse<ArticleDetailsDescDto, Object> getDesignationList(
			@RequestBody EmploymentDetailsRequest model) {
		return offsiteCustRegClient.getDesignationListResponse(model);
	}

	@RequestMapping(value = "/income_range/list", method = { RequestMethod.POST })
	public AmxApiResponse<IncomeRangeDto, Object> getIncomeRangeResponse(@RequestBody EmploymentDetailsRequest model) {
		return offsiteCustRegClient.getIncomeRangeResponse(model);
	}

	@RequestMapping(value = "/employment_type/list", method = { RequestMethod.POST })
	public AmxApiResponse<ComponentDataDto, Object> getEmploymentTypeList() {
		return offsiteCustRegClient.sendEmploymentTypeList();
	}

	@RequestMapping(value = "/kycdoc/submit", method = { RequestMethod.POST })
	public AmxApiResponse<String, Object> saveCustomeKycDocument(@RequestBody List<ImageSubmissionRequest> modelData)
			throws ParseException {
		return offsiteCustRegClient.saveCustomeKycDocument(modelData);
	}

	@RequestMapping(value = "/customer_info/save", method = { RequestMethod.POST })
	public AmxApiResponse<BigDecimal, Object> saveCustomerInfo(@RequestBody CustomerInfoRequest model) {
		return offsiteCustRegClient.saveCustomerInfo(model);
	}

	@RequestMapping(value = "/secques/list", method = { RequestMethod.GET })
	public AmxApiResponse<QuestModelDTO, Object> getSecQues() {
		return metaClient.getSequrityQuestion();
	}

	@RequestMapping(value = "/secques/set", method = { RequestMethod.POST })
	public AmxApiResponse<SecurityQuestionModel, Object> setSecQues(
			@RequestBody ListRequestModel<SecurityQuestionModel> req) {
		return AmxApiResponse.buildList(req.getValues());
	}

	@Autowired
	CustomerRegistrationClient customerRegistrationClient;

	@RequestMapping(value = "/phising/set", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> setPhising(@RequestParam String imageUrl,
			@RequestParam String caption) {
		return AmxApiResponse.build(customerRegistrationClient.savePhishiingImage(caption, imageUrl).getResult());
	}

	@RequestMapping(value = "/creds/set", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> setPhising(@RequestBody CustomerCredential req) {
		return AmxApiResponse.build(customerRegistrationClient.saveLoginDetail(req).getResult());
	}

	@RequestMapping(value = "/offsite-cust-reg/customer-mobile-email-validate-otp/", method = { RequestMethod.POST })
	public AmxApiResponse<String, Object> validateOtpForEmailAndMobile(
			@RequestBody OffsiteCustomerRegistrationRequest offsiteCustRegModel) {

		logger.info("Validate Otp for Email and Mobile request called for identity No : "
				+ offsiteCustRegModel.getIdentityInt() + " , mOtp : " + offsiteCustRegModel.getmOtp() + " and eOtp : "
				+ offsiteCustRegModel.geteOtp() + " and email : " + offsiteCustRegModel.getEmail() + " and mobile No : "
				+ offsiteCustRegModel.getMobile() + " and country id : " + offsiteCustRegModel.getCountryId()
				+ " and nationality id : " + offsiteCustRegModel.getNationalityId());

		return offsiteCustRegClient.validateOtpForEmailAndMobile(offsiteCustRegModel);
	}

	@RequestMapping(value = "/offsite-cust-reg/customer-mobile-email-send-otp/", method = { RequestMethod.POST })
	public AmxApiResponse<SendOtpModel, Object> sendOtpForEmailAndMobile(
			@RequestBody CustomerPersonalDetail customerPersonalDetail) {

		logger.info(
				"Send Otp for Email and Mobile request called for country id : " + customerPersonalDetail.getCountryId()
						+ " , nationality id : " + customerPersonalDetail.getNationalityId() + " and identity No : "
						+ customerPersonalDetail.getIdentityInt() + " and title : " + customerPersonalDetail.getTitle()
						+ " and first Name : " + customerPersonalDetail.getFirstName() + " and last Name : "
						+ customerPersonalDetail.getLastName() + " and email id : " + customerPersonalDetail.getEmail()
						+ " and mobile No : " + customerPersonalDetail.getMobile() + " and tel prefix : "
						+ customerPersonalDetail.getTelPrefix());

		return offsiteCustRegClient.sendOtpForEmailAndMobile(customerPersonalDetail);
	}

}
