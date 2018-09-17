package com.amx.jax.offsite.controller;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.OffsiteCustRegClient;
import com.amx.jax.model.request.CommonRequest;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.DynamicFieldRequest;
import com.amx.jax.model.request.EmploymentDetailsRequest;
import com.amx.jax.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.model.response.ArticleDetailsDescDto;
import com.amx.jax.model.response.ArticleMasterDescDto;
import com.amx.jax.model.response.ComponentDataDto;
import com.amx.jax.model.response.FieldListDto;
import com.amx.jax.model.response.IncomeRangeDto;

import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author lalittanwar
 *
 */
@RestController
public class OffsiteController {

	private Logger logger = Logger.getLogger(OffsiteController.class);
	
	@Autowired
	private OffsiteCustRegClient offsiteCustRegClient;

	@ApiOperation(value = "Index page")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		return "index";
	}
	
	@RequestMapping(value = "/offsite-cust-reg/new-field-list/", method = { RequestMethod.POST })
	public AmxApiResponse<Map<String, FieldListDto>, Object> getFieldList(@RequestBody DynamicFieldRequest model) {

		logger.info("field list request called for tenant : " + model.getTenant() + " , nationality : "
				+ model.getNationality() + " and component : " + model.getComponent());
		
		return offsiteCustRegClient.getFieldList(model);
	}
	
	@RequestMapping(value = "/offsite-cust-reg/incomeRangeList/", method = { RequestMethod.POST })
	public AmxApiResponse<IncomeRangeDto, Object> getIncomeRangeResponse(@RequestBody EmploymentDetailsRequest model) {

		logger.info("Income range request called for article id : " + model.getArticleId() + " , article details id : "
				+ model.getArticleDetailsId() + " and country id : " + model.getCountryId());
		
		return offsiteCustRegClient.getIncomeRangeResponse(model);
	}
	
	@RequestMapping(value = "/offsite-cust-reg/designationList/", method = { RequestMethod.POST })
	public AmxApiResponse<ArticleDetailsDescDto, Object> getDesignationListResponse(@RequestBody EmploymentDetailsRequest model) {

		logger.info("Designation list request called for article id : " + model.getArticleId() + " , article details id : "
				+ model.getArticleDetailsId() + " and country id : " + model.getCountryId());

		return offsiteCustRegClient.getDesignationListResponse(model);
	}
	
	@RequestMapping(value = "/offsite-cust-reg/articleList/", method = { RequestMethod.POST })
	public AmxApiResponse<ArticleMasterDescDto, Object> getArticleListResponse(@RequestBody CommonRequest model) {

		logger.info("Artcile list request called for country id : " + model.getCountryId() + " , state id : "
				+ model.getStateId() + " and district id : " + model.getDistrictId() + " and city id : " 
				+ model.getCityId()	+ " and nationality id : " + model.getNationalityId() );

		return offsiteCustRegClient.getArticleListResponse(model);
	}
	
	@RequestMapping(value = "/offsite-cust-reg/customer-mobile-email-validate-otp/", method = { RequestMethod.POST })
	public AmxApiResponse<String, Object> validateOtpForEmailAndMobile(@RequestBody OffsiteCustomerRegistrationRequest offsiteCustRegModel) {

		logger.info("Validate Otp for Email and Mobile request called for identity No : " + offsiteCustRegModel.getIdentityInt() + " , mOtp : "
				+ offsiteCustRegModel.getmOtp() + " and eOtp : " + offsiteCustRegModel.geteOtp() + " and email : " 
				+ offsiteCustRegModel.getEmail()	+ " and mobile No : " + offsiteCustRegModel.getMobile() + " and country id : " 
				+ offsiteCustRegModel.getCountryId() + " and nationality id : " + offsiteCustRegModel.getNationalityId());

		return offsiteCustRegClient.validateOtpForEmailAndMobile(offsiteCustRegModel);
	}
	
	@RequestMapping(value = "/offsite-cust-reg/employmentTypeList/", method = { RequestMethod.POST })
	public AmxApiResponse<ComponentDataDto, Object> sendEmploymentTypeList() {
		
		logger.info("Employee Type list request");
		
		return offsiteCustRegClient.sendEmploymentTypeList();
	}
	
	@RequestMapping(value = "/offsite-cust-reg/professionList/", method = { RequestMethod.POST })
	public AmxApiResponse<ComponentDataDto, Object> sendProfessionList() {
		
		logger.info("Profession list request");
		
		return offsiteCustRegClient.sendProfessionList();
	}
	
	@RequestMapping(value = "/offsite-cust-reg/send-id-types/", method = { RequestMethod.POST })
	public AmxApiResponse<ComponentDataDto, Object> sendIdTypes() {
		
		logger.info("send id type request");

		return offsiteCustRegClient.sendIdTypes();
	}
	
	@RequestMapping(value = "/offsite-cust-reg/customer-mobile-email-send-otp/", method = { RequestMethod.POST })
	public AmxApiResponse<List, Object> sendOtpForEmailAndMobile(CustomerPersonalDetail customerPersonalDetail){
		
		logger.info("Send Otp for Email and Mobile request called for country id : " + customerPersonalDetail.getCountryId() + " , nationality id : "
				+ customerPersonalDetail.getNationalityId() + " and identity No : " + customerPersonalDetail.getIdentityInt() + " and title : " 
				+ customerPersonalDetail.getTitle()	+ " and first Name : " + customerPersonalDetail.getFirstName() + " and last Name : " 
				+ customerPersonalDetail.getLastName() + " and email id : " + customerPersonalDetail.getEmail() + " and mobile No : " 
				+ customerPersonalDetail.getMobile() + " and tel prefix : " + customerPersonalDetail.getTelPrefix());

		return offsiteCustRegClient.sendOtpForEmailAndMobile(customerPersonalDetail);
	}

}