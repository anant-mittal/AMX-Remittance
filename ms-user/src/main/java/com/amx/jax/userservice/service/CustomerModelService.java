package com.amx.jax.userservice.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.JaxApiFlow;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.model.response.customer.CustomerFlags;
import com.amx.jax.model.response.customer.CustomerModelResponse;
import com.amx.jax.model.response.customer.PersonInfo;
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

	public CustomerModelResponse getCustomerModelResponse(String identityInt) {
		userValidationService.validateIdentityInt(identityInt, ConstantDocument.BIZ_COMPONENT_ID_CIVIL_ID);
		List<Customer> customers = userValidationService.validateNonActiveOrNonRegisteredCustomerStatus(identityInt,
				JaxApiFlow.SIGNUP_ONLINE);
		Customer customer = customers.get(0);
		BigDecimal customerId = customer.getCustomerId();
		PersonInfo personInfo = userService.getPersonInfo(customerId);
		CustomerFlags customerFlags = customerFlagManager.getCustomerFlags(customerId);
		CustomerModelResponse response = new CustomerModelResponse(personInfo, customerFlags);
		response.setCustomerId(customer.getCustomerId());
		return response;
	}

}
