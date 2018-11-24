package com.amx.jax.dao;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.dbmodel.ShoppingCartDetails;
import com.amx.jax.dbmodel.fx.FxExchangeRateView;
import com.amx.jax.repository.ParameterDetailsRespository;
import com.amx.jax.repository.ShoppingCartRepository;
import com.amx.jax.repository.fx.FcSaleExchangeRateRepository;


@Component
public class FcSaleExchangeRateDao {

	@Autowired
	FcSaleExchangeRateRepository fcSaleExchangeRateRepository;
	
	@Autowired
	ParameterDetailsRespository parameterDetailsRespository;
	
	@Autowired
	ShoppingCartRepository shoppingCartRepository;
	
	public List<FxExchangeRateView> getFcSaleExchangeRate(BigDecimal applicationCountryId,BigDecimal countryBranchId,BigDecimal fxCurrencyId){
		return fcSaleExchangeRateRepository.findByApplicationCountryIdAndCountryBranchIdAndFxCurrencyId(applicationCountryId, countryBranchId, fxCurrencyId);
	}
	
	
	public List<ParameterDetails> getParameterDetails(String recordId,String isactive){
		return parameterDetailsRespository.findByRecordIdAndIsActive(recordId, isactive);
	}
	

	public List<ShoppingCartDetails> getShoppingCartDetails(BigDecimal applicationcountryId,BigDecimal companyId,BigDecimal customerId){
		return shoppingCartRepository.findByApplicationCountryIdAndCompanyIdAndCustomerId(applicationcountryId,companyId,customerId);
	}
}
