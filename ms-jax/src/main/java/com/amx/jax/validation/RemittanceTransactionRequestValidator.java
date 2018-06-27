package com.amx.jax.validation;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.response.ExchangeRateBreakup;
import com.amx.amxlib.model.response.RemittanceTransactionResponsetModel;

@Component
public class RemittanceTransactionRequestValidator {

	public void validateExchangeRate(RemittanceTransactionRequestModel request,
			RemittanceTransactionResponsetModel response) {

		ExchangeRateBreakup oldExchangeRate = request.getExRateBreakup();
		ExchangeRateBreakup newExchangeRate = response.getExRateBreakup();
		if (oldExchangeRate.compareTo(newExchangeRate) != 0) {
			throw new GlobalException("Exchange rate has been changed", JaxError.EXCHANGE_RATE_CHANGED);
		}
	}

	public void validateFlexFields(RemittanceTransactionRequestModel request,
			Map<String, Object> remitApplParametersMap) {
		// TODO: check if any addition flex field is required
	}
}
