package com.amx.jax.services;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.model.response.ApiResponse;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerService extends AbstractService {

	@Override
	public String getModelType() {
		return "customer";
	}

	@Override
	public Class<?> getModelClass() {
		return this.getClass();
	}

	public ApiResponse validateNationalityId(String nationalityId) {
		ApiResponse response = this.getBlackApiResponse();
		return response;

	}

}
