package com.amx.jax.dao;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.FxExchangeRateView;
import com.amx.jax.repository.FcSaleExchangeRateRepository;


@Component
public class FcSaleExchangeRateDao {

	@Autowired
	FcSaleExchangeRateRepository fcSaleExchangeRateRepository;
	
	
	public List<FxExchangeRateView> getFcSaleExchangeRate(BigDecimal applicationCountryId,BigDecimal countryBranchId,BigDecimal fxCurrencyId){
		return fcSaleExchangeRateRepository.findByApplicationCountryIdAndCountryBranchIdAndFxCurrencyId(applicationCountryId, countryBranchId, fxCurrencyId);
	}

}
