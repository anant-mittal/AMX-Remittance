package com.amx.jax.userservice.manager;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.constant.JaxApiFlow;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.meta.MetaData;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.manager.CustomerRegistrationManager;
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
	
	
	public void saveCustomerSecQuestions(List<SecurityQuestionModel> securityQuestions) {
		
		CustomerOnlineRegistration customerOnlineRegistration = custDao.getOnlineCustByCustomerId(metaData.getCustomerId());
		if(customerOnlineRegistration == null) {
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
		CustomerOnlineRegistration onlineCustReg = custDao.getOnlineCustByCustomerId(customer.getCustomerId());
		userValidationService.validateBlackListedCustomerForLogin(customer);
		userValidationService.validateCustomerVerification(customer.getCustomerId());
		if (onlineCustReg != null) {
			userValidationService.validateCustomerLockCount(onlineCustReg);
			userValidationService.validateTokenDate(onlineCustReg);
		}
	}
}
