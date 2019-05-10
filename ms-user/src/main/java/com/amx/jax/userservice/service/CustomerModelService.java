package com.amx.jax.userservice.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.LoginLogoutHistory;
import com.amx.jax.model.response.customer.CustomerFlags;
import com.amx.jax.model.response.customer.CustomerModelResponse;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.manager.CustomerFlagManager;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerModelService {

	@Autowired
	UserValidationService userValidationService;
	@Autowired
	UserService userService;
	@Autowired
	CustomerFlagManager customerFlagManager;
	@Autowired
	CustomerDao custDao;

	public CustomerModelResponse getCustomerModelResponse(String identityInt) {
		//userValidationService.validateIdentityInt(identityInt, ConstantDocument.BIZ_COMPONENT_ID_CIVIL_ID);
		List<Customer> customers = userService.getCustomerByIdentityInt(identityInt);
		Customer customer = userValidationService.validateCustomerForDuplicateRecords(customers);
		BigDecimal customerId = customer.getCustomerId();
		PersonInfo personInfo = userService.getPersonInfo(customerId);
		CustomerOnlineRegistration cust = custDao.getOnlineCustByCustomerId(customerId);
		if(cust!=null) {
			LoginLogoutHistory history = userService.getLoginLogoutHistoryByUserName(cust.getUserName());
			if (history != null) {
				personInfo.setLastLoginTime(history.getLoginTime());
			}
		}
		
 		CustomerFlags customerFlags = customerFlagManager.getCustomerFlags(customerId);
		CustomerModelResponse response = new CustomerModelResponse(personInfo, customerFlags);
		response.setCustomerId(customer.getCustomerId());
		return response;
	}

}
