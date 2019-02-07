package com.amx.jax.branchremittance.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.util.JaxValidationUtil;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.remittance.BranchRemittanceGetExchangeRateRequest;
import com.amx.jax.model.response.remittance.branch.BranchRemittanceGetExchangeRateResponse;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BranchRemittanceExchangeRateManager {
	
	static final Logger LOGGER = LoggerFactory.getLogger(BranchRemittanceExchangeRateManager.class);

	public void validateGetExchangRateRequest(BranchRemittanceGetExchangeRateRequest request) {

		if (request.getForeignAmount() == null && request.getLocalAmount() == null) {
			throw new GlobalException(JaxError.INVALID_AMOUNT, "Either local or foreign amount must be present");
		}
		JaxValidationUtil.validatePositiveNumber(request.getForeignAmount(), "Foreign Amount should be positive",
				JaxError.INVALID_AMOUNT);
		JaxValidationUtil.validatePositiveNumber(request.getForeignAmount(), "Local Amount should be positive",
				JaxError.INVALID_AMOUNT);
		JaxValidationUtil.validatePositiveNumber(request.getCorrespondanceBankId(),
				"corespondance bank must be positive number");
		JaxValidationUtil.validatePositiveNumber(request.getBeneficiaryRelationshipSeqId(),
				"bene seq id bank must be positive number");
		JaxValidationUtil.validatePositiveNumber(request.getServiceIndicatorId(),
				"service indic id bank must be positive number");
	}

	public BranchRemittanceGetExchangeRateResponse getExchangeRateResponse(
			BranchRemittanceGetExchangeRateRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
}
