package com.amx.jax.userservice.service;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.model.CustomerHomeAddress;
import com.amx.amxlib.model.PersonInfo;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.CustomerCredential;
import com.amx.jax.dbmodel.ApplicationSetup;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.model.dto.SendOtpModel;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.repository.IApplicationCountryRepository;
import com.amx.jax.services.AbstractService;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.trnx.CustomerRegistrationTrnxModel;
import com.amx.jax.userservice.manager.CustomerRegistrationManager;
import com.amx.jax.userservice.manager.CustomerRegistrationOtpManager;
import com.amx.jax.userservice.repository.OnlineCustomerRepository;
import com.amx.jax.userservice.validation.CustomerCredentialValidator;
import com.amx.jax.userservice.validation.CustomerPersonalDetailValidator;
import com.amx.jax.userservice.validation.CustomerPhishigImageValidator;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.validation.CountryMetaValidation;

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
	@Autowired
	CustomerPhishigImageValidator customerPhishigImageValidator;
	@Autowired
	CustomerCredentialValidator customerCredentialValidator;
	@Autowired
	CountryMetaValidation countryMetaValidation;	
	@Autowired
	JaxNotificationService jaxNotificationService;
	@Autowired
	IApplicationCountryRepository applicationSetup;
	@Autowired
	UserService userService ; 
	@Autowired
	OnlineCustomerRepository onlineCustomer;
	
	/**
	 * Sends otp initiating trnx
	 */
	public ApiResponse sendOtp(CustomerPersonalDetail customerPersonalDetail) {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(customerPersonalDetail,"customerPersonalDetail");
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

	/**
	 * Save the customer home address
	 */
	public ApiResponse saveCustomerHomeAddress(CustomerHomeAddress customerHomeAddress) {
		countryMetaValidation.validateMobileNumberLength(customerHomeAddress.getCountryId(),
				customerHomeAddress.getMobile());
		customerRegistrationManager.saveHomeAddress(customerHomeAddress);
		return getBooleanResponse();
	}

	/**
	 * Saves the customer security question and answer
	 */
	public ApiResponse saveCustomerSecQuestions(List<SecurityQuestionModel> securityquestions) {
		customerRegistrationManager.saveCustomerSecQuestions(securityquestions);
		return getBooleanResponse();
	}

	/**
	 * @param caption
	 *            caption
	 * @param imageUrl
	 *            image url
	 */
	public ApiResponse savePhishingImage(String caption, String imageUrl) {
		CustomerRegistrationTrnxModel model = customerRegistrationManager.setPhishingImage(caption, imageUrl);
		customerPhishigImageValidator.validate(model, null);
		customerRegistrationManager.save(model);
		return getBooleanResponse();
	}

	/**
	 * @param -
	 *            customerCredential user id and password of cusotmer
	 *            <p>
	 *            commit trnx
	 *            </p>
	 */
	public ApiResponse saveLoginDetail(CustomerCredential customerCredential) {
		customerRegistrationManager.saveLoginDetail(customerCredential);
		//customerCredentialValidator.validate(customerRegistrationManager.get(),  null);
		//customerRegistrationManager.commit();	
	/*	
		Customer customerDetails = userService.getCustomerDetails(customerCredential.getLoginId());
		
		CustomerOnlineRegistration loginCustomer = onlineCustomer.getLoginCustomersById(customerDetails.getCustomerId());
		

		
		
		CustomerOnlineRegistration loginCustomer = onlineCustomer.getLoginCustomersById(customerCredential.getLoginId());
		
		Customer customerDetails = userService.getCustomerDetails(loginCustomer.getUserName());
		*/
		CustomerOnlineRegistration loginCustomer = onlineCustomer.getLoginCustomersById(customerCredential.getLoginId());
		Customer customerDetails = userService.getCustomerDetails(loginCustomer.getUserName());
		ApplicationSetup applicationSetupData = applicationSetup.getApplicationSetupDetails();
		PersonInfo personinfo = new PersonInfo();
		try {
			BeanUtils.copyProperties(personinfo, customerDetails);
		} catch (Exception e) {
		}
		jaxNotificationService.sendPartialRegistraionMail(personinfo, applicationSetupData);
		return getBooleanResponse();
	}
}
