package com.amx.jax.branchremittance.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.remittance.BranchRemittanceGetExchangeRateRequest;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BranchRemittanceExchangeRateManager {

	static final Logger LOGGER = LoggerFactory.getLogger(BranchRemittanceExchangeRateManager.class);

	public void validateGetExchangRateRequest(BranchRemittanceGetExchangeRateRequest request) {

		if (request.getForeignAmount() == null && request.getLocalAmount() == null) {
			throw new GlobalException(JaxError.INVALID_AMOUNT, "Either local or foreign amount must be present");
		}
		if(request.getLocalAmount() != null && request.getLocalAmount().doubleValue() < 0) {
			throw new GlobalException(JaxError.INVALID_AMOUNT, "Either local or foreign amount must be present");
		}
	}
}
