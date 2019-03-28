package com.amx.jax.userservice.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.model.response.customer.CustomerModelResponse;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerModelService {

	public CustomerModelResponse getCustomerModelResponse(String identityInt) {
		// TODO Auto-generated method stub
		return null;
	}

}
