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
			throw new GlobalException(JaxError. NULL_APPLICATION_COUNTRY_ID, "Country Id not found");
		}
	}
	
	
	public void fcSaleExchangeRate(BigDecimal countryId,BigDecimal countryBranchId,BigDecimal currencyId){
		if (!JaxUtil.isNullZeroBigDecimalCheck(countryId)) {
			throw new GlobalException(JaxError. NULL_APPLICATION_COUNTRY_ID, "Country Id not found");
		}
		
		if (!JaxUtil.isNullZeroBigDecimalCheck(countryBranchId)) {
			throw new GlobalException(JaxError.BLANK_COUNTRY_BRANCH, "Country branch Id not found");
		}
		
		if (!JaxUtil.isNullZeroBigDecimalCheck(currencyId)) {
			throw new GlobalException(JaxError. NULL_CURRENCY_ID, "currency not found");
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
			throw new GlobalException(JaxError.NULL_APPLICATION_COUNTRY_ID, "Null applicationCountryId  passed");
		}else{
			if(!JaxUtil.isNullZeroBigDecimalCheck(applicationCountryDb) && applicationCountryDb.compareTo(countryId)!=0){
				throw new GlobalException(JaxError.INVALID_APPLICATION_COUNTRY_ID, "invalid applciation country Id");
			}
		}
		
		if (!JaxUtil.isNullZeroBigDecimalCheck(countryBranchId)) {
			throw new GlobalException(JaxError.BLANK_COUNTRY_BRANCH, "Invalid Country branch Id");
		}else{
			
		}
		
		if (!JaxUtil.isNullZeroBigDecimalCheck(currencyId)) {
			throw new GlobalException(JaxError.NULL_CURRENCY_ID, "Null local currency id passed ");
		}else{
			if(!JaxUtil.isNullZeroBigDecimalCheck(localcurrencyDB) && localcurrencyDB.compareTo(currencyId)!=0){
				throw new GlobalException(JaxError.INVALID_CURRENCY_ID, "Invalid local currency Id");
			}
		}
		
		 if (customerId == null) {
				throw new GlobalException(JaxError.NULL_CUSTOMER_ID, "Null customer id passed ");
			}
		if (!JaxUtil.isNullZeroBigDecimalCheck(languageId)) {
			throw new GlobalException(JaxError. BLANK_LANGUAGE_ID, "Invalid language id");
		}else{
			
			}
		
		if (!JaxUtil.isNullZeroBigDecimalCheck(companyId)) {
			throw new GlobalException(JaxError. BLANK_COMPANY_ID, "Invalid company id");
		}else{
			if(!JaxUtil.isNullZeroBigDecimalCheck(companyIdDb) && companyIdDb.compareTo(companyId)!=0){
				throw new GlobalException(JaxError.INVALID_COMPANY_ID, "invalid comapany");
			}
		}
	}
}


