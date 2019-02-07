/**
 * 
 */
package com.amx.jax.branchremittance.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branchremittance.manager.BranchRemittanceExchangeRateManager;
import com.amx.jax.model.request.remittance.BranchRemittanceGetExchangeRateRequest;
import com.amx.jax.model.response.remittance.branch.BranchRemittanceGetExchangeRateResponse;

/**
 * @author Prashant
 *
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BranchRemittanceExchangeRateService {

	static final Logger LOGGER = LoggerFactory.getLogger(BranchRemittanceExchangeRateService.class);

	@Autowired
	BranchRemittanceExchangeRateManager branchRemittanceExchangeRateManager;

	public AmxApiResponse<BranchRemittanceGetExchangeRateResponse, Object> getExchaneRate(BranchRemittanceGetExchangeRateRequest request) {
		branchRemittanceExchangeRateManager.validateGetExchangRateRequest(request);
		BranchRemittanceGetExchangeRateResponse result = branchRemittanceExchangeRateManager.getExchangeRateResponse(request);

		return AmxApiResponse.build(result);
	}
}
