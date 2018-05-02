package com.amx.jax.userservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.model.CustomerHomeAddress;
import com.amx.amxlib.model.CustomerPersonalDetail;
import com.amx.amxlib.model.SendOtpModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.services.AbstractService;
import com.amx.jax.trnx.CustomerRegistrationTrnxModel;
import com.amx.jax.userservice.manager.CustomerRegistrationManager;
import com.amx.jax.userservice.manager.CustomerRegistrationOtpManager;
import com.amx.jax.userservice.validation.CustomerPersonalDetailValidator;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.JaxUtil;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class CustomerRegistrationService extends AbstractService {

	public static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationService.class);

	@Override
	public String getModelType() {
		return "customer-registration";
	}

	@Autowired
	JaxUtil util;

	@Autowired
	CryptoUtil cryptoUtil;

	@Autowired
	CustomerRegistrationManager customerRegistrationManager;

	@Autowired
	CustomerPersonalDetailValidator customerPersonalDetailValidator;

	@Autowired
	CustomerRegistrationOtpManager customerRegistrationOtpManager;

	/**
	 * Sends otp initiating trnx
	 */
	public ApiResponse sendOtp(CustomerPersonalDetail customerPersonalDetail) {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(customerPersonalDetail,
				"customerPersonalDetail");
		customerRegistrationManager.setIdentityInt(customerPersonalDetail.getIdentityInt());
		// initiate transaction
		CustomerRegistrationTrnxModel trnxModel = customerRegistrationManager.init(customerPersonalDetail);
		customerPersonalDetailValidator.validate(trnxModel, errors);
		ApiResponse apiResponse = getBlackApiResponse();
		SendOtpModel output = customerRegistrationOtpManager.generateOtpTokens(customerPersonalDetail.getIdentityInt());
		customerRegistrationOtpManager.sendOtp();
		apiResponse.getData().getValues().add(output);
		apiResponse.getData().setType("send-otp-model");
		return apiResponse;
	}

	/**
	 * validates otp
	 */
	public ApiResponse validateOtp(String mOtp, String eOtp) {
		customerRegistrationOtpManager.validateOtp(mOtp, eOtp);
		return getBooleanResponse();
	}

	public ApiResponse saveCustomerHomeAddress(CustomerHomeAddress customerHomeAddress) {
		customerRegistrationManager.saveHomeAddress(customerHomeAddress);
		return getBooleanResponse();
	}
}
