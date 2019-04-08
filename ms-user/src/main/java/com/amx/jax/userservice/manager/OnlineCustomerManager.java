package com.amx.jax.userservice.manager;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.meta.MetaData;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.manager.CustomerRegistrationManager;
import com.amx.jax.userservice.service.UserService;
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
	
	
	public void saveCustomerSecQuestions(List<SecurityQuestionModel> securityQuestions) {
		Customer customer = custDao.getCustById(metaData.getCustomerId());
		CustomerOnlineRegistration customerOnlineRegistration = new CustomerOnlineRegistration(customer);
		userService.simplifyAnswers(securityQuestions);
		custDao.setSecurityQuestions(securityQuestions, customerOnlineRegistration);
		custDao.saveOnlineCustomer(customerOnlineRegistration);
	}
}
