package com.amx.jax.exrateservice.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.PipsMstr;
import com.amx.jax.exrateservice.repository.PipsMstrRepository;

@Component
public class PipsMstrDao {

	
	@Autowired
	PipsMstrRepository repo;

	public void saveOrUpdate(PipsMstr exRate) {
			repo.save(exRate);		
	}
	
	public List<PipsMstr> getExchangeRatesPlaceorder(BigDecimal currency, String bankName) {
		List<PipsMstr> exchangeRates = repo.getExchangeRatesPlaceorder(currency, bankName);
	    return exchangeRates;
	}
	
}
