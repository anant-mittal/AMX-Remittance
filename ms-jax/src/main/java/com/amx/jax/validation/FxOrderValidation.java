package com.amx.jax.validation;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;

import com.amx.jax.error.JaxError;
import com.amx.jax.util.JaxUtil;

@Component
public class FxOrderValidation {
	
	public void fcsalecurrencyList(BigDecimal countryId){
		if (!JaxUtil.isNullZeroBigDecimalCheck(countryId)) {
			throw new GlobalException("Country Id not found", JaxError. NULL_APPLICATION_COUNTRY_ID);
		}
	}
	
	
	public void fcSaleExchangeRate(BigDecimal countryId,BigDecimal countryBranchId,BigDecimal currencyId){
		if (!JaxUtil.isNullZeroBigDecimalCheck(countryId)) {
			throw new GlobalException("Country Id not found", JaxError. NULL_APPLICATION_COUNTRY_ID);
		}
		
		if (!JaxUtil.isNullZeroBigDecimalCheck(countryBranchId)) {
			throw new GlobalException("Country branch Id not found", JaxError.NULL_COUNTRY_BRANCH);
		}
		
		if (!JaxUtil.isNullZeroBigDecimalCheck(currencyId)) {
			throw new GlobalException("currency not found", JaxError. NULL_CURRENCY_ID);
		}
	}
}
