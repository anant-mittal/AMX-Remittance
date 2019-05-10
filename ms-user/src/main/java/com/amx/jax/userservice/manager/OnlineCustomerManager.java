package com.amx.jax.userservice.manager;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.JaxApiFlow;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dict.ContactType;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.SecurityQuestionModel;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.userservice.service.UserValidationService;

@Component
public class OnlineCustomerManager {

	@Autowired
	CustomerRegistrationManager customerRegistrationManager;
	@Autowired
	CustomerDao custDao;
	@Autowired
	MetaData metaData;
	@Autowired
	UserService userService;
	@Autowired
	UserValidationService userValidationService;
	@Autowired
	CustomerAuthManager customerAuthManager;

	public void saveCustomerSecQuestions(List<SecurityQuestionModel> securityQuestions) {
		customerAuthManager.validateAndSendOtp(Arrays.asList(ContactType.SMS));
		CustomerOnlineRegistration customerOnlineRegistration = custDao
				.getOnlineCustByCustomerId(metaData.getCustomerId());
		if (customerOnlineRegistration == null) {
			throw new GlobalException("Online customer not found");
		}
		userService.simplifyAnswers(securityQuestions);
		custDao.setSecurityQuestions(securityQuestions, customerOnlineRegistration);
		custDao.saveOnlineCustomer(customerOnlineRegistration);
	}

	public void doSignUpValidations(String identityInt) {
		List<Customer> customers = userValidationService.validateNonActiveOrNonRegisteredCustomerStatus(identityInt,
				JaxApiFlow.SIGNUP_ONLINE);
		Customer customer = customers.get(0);
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(customer.getCustomerId());
		if (onlineCustomer != null && ConstantDocument.Yes.equals(onlineCustomer.getStatus())) {
			throw new GlobalException(JaxError.USER_ALREADY_ACTIVE,
					"You are already registered with us. Please login.");
		}
		userValidationService.validateCustIdProofs(customer.getCustomerId());
		userValidationService.validateBlackListedCustomerForLogin(customer);
		userValidationService.validateCustomerVerification(customer.getCustomerId());
	}
}
