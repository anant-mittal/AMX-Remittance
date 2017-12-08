package com.amx.jax.exrateservice.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.ExchangeRateApprovalDetModel;
import com.amx.jax.exrateservice.repository.ExchangeRateApprovalDetRepository;

@Component
public class ExchangeRateDao {

	@Autowired
	private ExchangeRateApprovalDetRepository repo;

	public List<ExchangeRateApprovalDetModel> getExchangeRates(BigDecimal currencyId) {

		List<ExchangeRateApprovalDetModel> exchangeRates = repo.getExchangeRates(currencyId);
		return exchangeRates;
	}

}
