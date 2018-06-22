package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.CUSTOMER_REG_ENDPOINT;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.CustomerCredential;
import com.amx.amxlib.model.CustomerHomeAddress;
import com.amx.amxlib.model.CustomerPersonalDetail;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.userservice.service.CustomerRegistrationService;


/**
 * @author Prashant
 *
 */
@RestController
@RequestMapping(CUSTOMER_REG_ENDPOINT)
@SuppressWarnings("rawtypes")
public class CustomerRegistrationController {

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationController.class);

	/**
	 * Sends otp to customers email and mobile initiating transaction
	 */
	@RequestMapping(value = "/send-otp/", method = RequestMethod.POST)
	public ApiResponse sendOtp(@RequestBody CustomerPersonalDetail customerPersonalDetail) {
		logger.info("send otp request: " + customerPersonalDetail);
		ApiResponse response = customerRegistrationService.sendOtp(customerPersonalDetail);
		return response;
	}

	/**
	 * validates otp
	 */
	@RequestMapping(value = "/validate-otp/", method = RequestMethod.POST)
	public ApiResponse validateOtp(@RequestParam String mOtp, @RequestParam String eOtp) {
		logger.info("validateOtp request: mOtp {} , eOtp {}", mOtp, eOtp);
		ApiResponse response = customerRegistrationService.validateOtp(mOtp, eOtp);
		return response;
	}

	/**
	 * save customer home addr
	 */
	@RequestMapping(value = "/save-home-addr/", method = RequestMethod.POST)
	public ApiResponse saveCustomerHomeAddress(@RequestBody CustomerHomeAddress customerHomeAddress) {
		logger.info("in saveCustomerHomeAddress: {} ", customerHomeAddress);
		ApiResponse response = customerRegistrationService.saveCustomerHomeAddress(customerHomeAddress);
		return response;
	}

	/**
	 * save customer security questions
	 */
	@RequestMapping(value = "/save-security-questions/", method = RequestMethod.POST)
	public ApiResponse saveCustomerSecQuestions(@RequestBody List<SecurityQuestionModel> securityquestions) {
		logger.info("in securityquestions: ");
		ApiResponse response = customerRegistrationService.saveCustomerSecQuestions(securityquestions);
		return response;
	}
	
	/**
	 * save savePhishingImage
	 */
	@RequestMapping(value = "/save-phishing-image/", method = RequestMethod.POST)
	public ApiResponse savePhishingImage(@RequestParam String caption, @RequestParam String imageUrl) {
		logger.info("in savePhishingImage: ");
		ApiResponse response = customerRegistrationService.savePhishingImage(caption, imageUrl);
		return response;
	}
	

	/**
	 * save credentails
	 */
	@RequestMapping(value = "/save-login-detail/", method = RequestMethod.POST)
	public ApiResponse saveLoginDetail(@RequestBody @Valid CustomerCredential customerCredential ) {
		logger.info("in saveLoginDetail: ");
		ApiResponse response = customerRegistrationService.saveLoginDetail(customerCredential);
		return response;
	}
	
}
