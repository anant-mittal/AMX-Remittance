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
import com.amx.jax.offsite.service.OffsiteService;

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
	@Deprecated
	private OffsiteService offsiteService;
	
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
		//Map<String, FieldListDto> mapFieldList = offsiteService.getFieldList(model);

		//return AmxApiResponse.build(mapFieldList);
		return offsiteCustRegClient.getFieldList(model);
	}
	
	@RequestMapping(value = "/offsite-cust-reg/incomeRangeList/", method = { RequestMethod.POST })
	public AmxApiResponse<IncomeRangeDto, Object> getIncomeRangeResponse(@RequestBody EmploymentDetailsRequest model) {

		logger.info("Income range request called for article id : " + model.getArticleId() + " , article details id : "
				+ model.getArticleDetailsId() + " and country id : " + model.getCountryId());
		//IncomeRangeDto incomeRangeDto = offsiteService.getIncomeRangeResponse(model);

		//return AmxApiResponse.build(incomeRangeDto);
		return offsiteCustRegClient.getIncomeRangeResponse(model);
	}
	
	@RequestMapping(value = "/offsite-cust-reg/designationList/", method = { RequestMethod.POST })
	public AmxApiResponse<ArticleDetailsDescDto, Object> getDesignationListResponse(@RequestBody EmploymentDetailsRequest model) {

		logger.info("Designation list request called for article id : " + model.getArticleId() + " , article details id : "
				+ model.getArticleDetailsId() + " and country id : " + model.getCountryId());
		ArticleDetailsDescDto articleDetailsDescDto = offsiteService.getDesignationListResponse(model);

		return AmxApiResponse.build(articleDetailsDescDto);
	}
	
	@RequestMapping(value = "/offsite-cust-reg/articleList/", method = { RequestMethod.POST })
	public AmxApiResponse<ArticleMasterDescDto, Object> getArticleListResponse(@RequestBody CommonRequest model) {

		logger.info("Artcile list request called for country id : " + model.getCountryId() + " , state id : "
				+ model.getStateId() + " and district id : " + model.getDistrictId() + " and city id : " 
				+ model.getCityId()	+ " and nationality id : " + model.getNationalityId() );
		ArticleMasterDescDto articleMasterDescDto = offsiteService.getArticleListResponse(model);

		return AmxApiResponse.build(articleMasterDescDto);
	}
	
	@RequestMapping(value = "/offsite-cust-reg/customer-mobile-email-validate-otp/", method = { RequestMethod.POST })
	public AmxApiResponse<String, Object> validateOtpForEmailAndMobile(@RequestBody OffsiteCustomerRegistrationRequest offsiteCustRegModel) {

		logger.info("Validate Otp for Email and Mobile request called for identity No : " + offsiteCustRegModel.getIdentityInt() + " , mOtp : "
				+ offsiteCustRegModel.getmOtp() + " and eOtp : " + offsiteCustRegModel.geteOtp() + " and email : " 
				+ offsiteCustRegModel.getEmail()	+ " and mobile No : " + offsiteCustRegModel.getMobile() + " and country id : " 
				+ offsiteCustRegModel.getCountryId() + " and nationality id : " + offsiteCustRegModel.getNationalityId());
		String obj = offsiteService.validateOtpForEmailAndMobile(offsiteCustRegModel);

		return AmxApiResponse.build(obj);
	}
	
	@RequestMapping(value = "/offsite-cust-reg/employmentTypeList/", method = { RequestMethod.POST })
	public AmxApiResponse<ComponentDataDto, Object> sendEmploymentTypeList() {
		
		logger.info("Employee Type list request");
		ComponentDataDto componentDataDto = offsiteService.sendEmploymentTypeList();
		
		return AmxApiResponse.build(componentDataDto);
	}
	
	@RequestMapping(value = "/offsite-cust-reg/professionList/", method = { RequestMethod.POST })
	public AmxApiResponse<ComponentDataDto, Object> sendProfessionList() {
		
		logger.info("Profession list request");
		ComponentDataDto componentDataDto = offsiteService.sendProfessionList();
		
		return AmxApiResponse.build(componentDataDto);
	}
	
	@RequestMapping(value = "/offsite-cust-reg/send-id-types/", method = { RequestMethod.POST })
	public AmxApiResponse<ComponentDataDto, Object> sendIdTypes() {
		
		logger.info("send id type request");
		ComponentDataDto componentDataDto = offsiteService.sendIdTypes();

		return AmxApiResponse.build(componentDataDto);
	}
	
	@RequestMapping(value = "/offsite-cust-reg/customer-mobile-email-send-otp/", method = { RequestMethod.POST })
	public AmxApiResponse<List, Object> sendOtpForEmailAndMobile(CustomerPersonalDetail customerPersonalDetail){
		
		logger.info("Send Otp for Email and Mobile request called for country id : " + customerPersonalDetail.getCountryId() + " , nationality id : "
				+ customerPersonalDetail.getNationalityId() + " and identity No : " + customerPersonalDetail.getIdentityInt() + " and title : " 
				+ customerPersonalDetail.getTitle()	+ " and first Name : " + customerPersonalDetail.getFirstName() + " and last Name : " 
				+ customerPersonalDetail.getLastName() + " and email id : " + customerPersonalDetail.getEmail() + " and mobile No : " 
				+ customerPersonalDetail.getMobile() + " and tel prefix : " + customerPersonalDetail.getTelPrefix());
		List obj = offsiteService.sendOtpForEmailAndMobile(customerPersonalDetail);

		return AmxApiResponse.build(obj);
	}

}
