package com.amx.jax.dao;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.FxExchangeRateView;
import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.repository.FcSaleExchangeRateRepository;
import com.amx.jax.repository.ParameterDetailsRespository;


@Component
public class FcSaleExchangeRateDao {

	@Autowired
	FcSaleExchangeRateRepository fcSaleExchangeRateRepository;
	
	@Autowired
	ParameterDetailsRespository parameterDetailsRespository;
	
	
	public List<FxExchangeRateView> getFcSaleExchangeRate(BigDecimal applicationCountryId,BigDecimal countryBranchId,BigDecimal fxCurrencyId){
		return fcSaleExchangeRateRepository.findByApplicationCountryIdAndCountryBranchIdAndFxCurrencyId(applicationCountryId, countryBranchId, fxCurrencyId);
	}
	
	
	public List<ParameterDetails> getParameterDetails(String recordId,String isactive){
		return parameterDetailsRespository.findByRecordIdAndIsActive(recordId, isactive);
	}
	

}
