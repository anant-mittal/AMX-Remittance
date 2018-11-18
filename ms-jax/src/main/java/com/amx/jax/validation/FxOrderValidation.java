package com.amx.jax.validation;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.util.JaxUtil;

@Component
public class FxOrderValidation {
	
	@Autowired
	MetaData metaData;
	
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
			throw new GlobalException("Country branch Id not found", JaxError.BLANK_COUNTRY_BRANCH);
		}
		
		if (!JaxUtil.isNullZeroBigDecimalCheck(currencyId)) {
			throw new GlobalException("currency not found", JaxError. NULL_CURRENCY_ID);
		}
	}
	
	
	public void validateHeaderInfo(){
		
		BigDecimal currencyId = metaData.getDefaultCurrencyId();
		BigDecimal countryId = metaData.getCountryId();
		BigDecimal countryBranchId = metaData.getCountryBranchId();
		BigDecimal companyId = metaData.getCompanyId();
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal languageId = metaData.getLanguageId();
		
		if (!JaxUtil.isNullZeroBigDecimalCheck(countryId)) {
			throw new GlobalException("Null applicationCountryId  passed", JaxError.NULL_APPLICATION_COUNTRY_ID);
		}
		
		if (!JaxUtil.isNullZeroBigDecimalCheck(countryBranchId)) {
			throw new GlobalException("Invalid Country branch Id", JaxError.BLANK_COUNTRY_BRANCH);
		}
		
		if (!JaxUtil.isNullZeroBigDecimalCheck(currencyId)) {
			throw new GlobalException("Null local currency id passed ", JaxError.NULL_CURRENCY_ID);
		}
		
		 if (customerId == null) {
				throw new GlobalException("Null customer id passed ", JaxError.NULL_CUSTOMER_ID);
			}
		if (!JaxUtil.isNullZeroBigDecimalCheck(languageId)) {
			throw new GlobalException("Invalid language id", JaxError. BLANK_LANGUAGE_ID);
		}
		
		if (!JaxUtil.isNullZeroBigDecimalCheck(companyId)) {
			throw new GlobalException("Invalid company id", JaxError. BLANK_COMPANY_ID);
		}
	}
}


