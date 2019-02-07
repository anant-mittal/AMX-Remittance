package com.amx.jax.branchremittance.manager;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.amxlib.util.JaxValidationUtil;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.error.JaxError;
import com.amx.jax.exrateservice.service.JaxDynamicPriceService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.BranchRemittanceGetExchangeRateRequest;
import com.amx.jax.model.response.remittance.branch.BranchRemittanceGetExchangeRateResponse;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.services.BeneficiaryValidationService;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BranchRemittanceExchangeRateManager {

	static final Logger LOGGER = LoggerFactory.getLogger(BranchRemittanceExchangeRateManager.class);

	@Autowired
	JaxDynamicPriceService jaxDynamicPriceService;
	@Autowired
	BeneficiaryService beneficiaryService;
	@Autowired
	MetaData metaData;
	@Autowired
	BeneficiaryValidationService beneValidationService;

	public void validateGetExchangRateRequest(BranchRemittanceGetExchangeRateRequest request) {

		if (request.getForeignAmount() == null && request.getLocalAmount() == null) {
			throw new GlobalException(JaxError.INVALID_AMOUNT, "Either local or foreign amount must be present");
		}
		JaxValidationUtil.validatePositiveNumber(request.getForeignAmount(), "Foreign Amount should be positive", JaxError.INVALID_AMOUNT);
		JaxValidationUtil.validatePositiveNumber(request.getForeignAmount(), "Local Amount should be positive", JaxError.INVALID_AMOUNT);
		JaxValidationUtil.validatePositiveNumber(request.getCorrespondanceBankId(), "corespondance bank must be positive number");
		JaxValidationUtil.validatePositiveNumber(request.getBeneficiaryRelationshipSeqId(), "bene seq id bank must be positive number");
		JaxValidationUtil.validatePositiveNumber(request.getServiceIndicatorId(), "service indic id bank must be positive number");
	}

	public BranchRemittanceGetExchangeRateResponse getExchangeRateResponse(BranchRemittanceGetExchangeRateRequest request) {
		BenificiaryListView beneficiaryView = beneValidationService.validateBeneficiary(request.getBeneficiaryRelationshipSeqId());
		ExchangeRateResponseModel exchangeRateResponseModel = jaxDynamicPriceService.getExchangeRates(metaData.getDefaultCurrencyId(),
				beneficiaryView.getCurrencyId(), request.getLocalAmount(), request.getForeignAmount(), beneficiaryView.getCountryId(),
				new BigDecimal(request.getCorrespondanceBankId()), new BigDecimal(request.getServiceIndicatorId()));
		if(exchangeRateResponseModel.getExRateBreakup() == null) {
			throw new GlobalException(JaxError.EXCHANGE_RATE_NOT_FOUND, "No exchange data found");
		}
		BranchRemittanceGetExchangeRateResponse result = new BranchRemittanceGetExchangeRateResponse();
		result.setExRateBreakup(exchangeRateResponseModel.getExRateBreakup());
		// NEED TO CHANGE THIS Hard coded just for demo
		// TODO
		result.setTxnFee(BigDecimal.ONE);
		return result;
	}
}
