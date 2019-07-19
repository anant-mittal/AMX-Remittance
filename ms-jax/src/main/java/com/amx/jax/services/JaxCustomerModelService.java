package com.amx.jax.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.insurance.GigInsuranceService;
import com.amx.jax.model.response.customer.CustomerFlags;
import com.amx.jax.model.response.customer.CustomerModelResponse;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JaxCustomerModelService {

	@Autowired
	GigInsuranceService gigInsuranceService;

	public void updateCustomerModelResponse(CustomerModelResponse customerModelResponse) {
		// update insurance flag
		CustomerFlags customerFlag = customerModelResponse.getCustomerFlags();
		if (gigInsuranceService.isInsuranceActive(customerModelResponse.getCustomerId())) {
			customerFlag.setForceUpdateInsurance(!gigInsuranceService.hasAddedNominee(customerModelResponse.getCustomerId()));
		}
	}
}
