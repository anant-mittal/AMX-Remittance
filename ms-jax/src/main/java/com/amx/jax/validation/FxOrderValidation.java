package com.amx.jax.validation;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.dbmodel.ApplicationSetup;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.IApplicationCountryRepository;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.util.JaxUtil;

@Component
public class FxOrderValidation {
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	IApplicationCountryRepository applCountryRepos;
	
	@Autowired
	ICurrencyDao currencyDao;
	

	
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
		ApplicationSetup appl = applCountryRepos.getApplicationSetupDetails();
		
		BigDecimal currencyId = metaData.getDefaultCurrencyId();
		BigDecimal countryId = metaData.getCountryId();
		BigDecimal countryBranchId = metaData.getCountryBranchId();
		BigDecimal companyId = metaData.getCompanyId();
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal languageId =new BigDecimal(1);
		BigDecimal applicationCountryDb = BigDecimal.ZERO;
		BigDecimal companyIdDb =BigDecimal.ZERO;
		BigDecimal localcurrencyDB = BigDecimal.ZERO;
		
		
		if(appl!= null){
			applicationCountryDb = appl.getApplicationCountryId();
			companyIdDb =appl.getCompanyId();
		}
		if(!currencyDao.getCurrencyListByCountryId(applicationCountryDb).isEmpty()) {
			localcurrencyDB = currencyDao.getCurrencyListByCountryId(applicationCountryDb).get(0).getCurrencyId();
		}
		
		if (!JaxUtil.isNullZeroBigDecimalCheck(countryId)) {
			throw new GlobalException("Null applicationCountryId  passed", JaxError.NULL_APPLICATION_COUNTRY_ID);
		}else{
			if(!JaxUtil.isNullZeroBigDecimalCheck(applicationCountryDb) && applicationCountryDb.compareTo(countryId)!=0){
				throw new GlobalException("invalid applciation country Id", JaxError.INVALID_APPLICATION_COUNTRY_ID);
			}
		}
		
		if (!JaxUtil.isNullZeroBigDecimalCheck(countryBranchId)) {
			throw new GlobalException("Invalid Country branch Id", JaxError.BLANK_COUNTRY_BRANCH);
		}else{
			
		}
		
		if (!JaxUtil.isNullZeroBigDecimalCheck(currencyId)) {
			throw new GlobalException("Null local currency id passed ", JaxError.NULL_CURRENCY_ID);
		}else{
			if(!JaxUtil.isNullZeroBigDecimalCheck(localcurrencyDB) && localcurrencyDB.compareTo(currencyId)!=0){
				throw new GlobalException("Invalid local currency Id", JaxError.INVALID_CURRENCY_ID);
			}
		}
		
		 if (customerId == null) {
				throw new GlobalException("Null customer id passed ", JaxError.NULL_CUSTOMER_ID);
			}
		if (!JaxUtil.isNullZeroBigDecimalCheck(languageId)) {
			throw new GlobalException("Invalid language id", JaxError. BLANK_LANGUAGE_ID);
		}else{
			
			}
		
		if (!JaxUtil.isNullZeroBigDecimalCheck(companyId)) {
			throw new GlobalException("Invalid company id", JaxError. BLANK_COMPANY_ID);
		}else{
			if(!JaxUtil.isNullZeroBigDecimalCheck(companyIdDb) && companyIdDb.compareTo(companyId)!=0){
				throw new GlobalException("invalid comapany", JaxError.INVALID_COMPANY_ID);
			}
		}
	}
}


