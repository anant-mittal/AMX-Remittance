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

	public List<ExchangeRateApprovalDetModel> getExchangeRates(BigDecimal currencyId, BigDecimal countryBranchId,
			BigDecimal countryId) {

		List<ExchangeRateApprovalDetModel> exchangeRates = repo.getExchangeRates(currencyId, countryBranchId,
				countryId);
		return exchangeRates;
	}

	public List<ExchangeRateApprovalDetModel> getExchangeRatesForRoutingBank(BigDecimal currencyId, BigDecimal countryBranchId,
			BigDecimal countryId, BigDecimal applicationCountryId, BigDecimal routingBankId,
			BigDecimal serviceIndicatorId) {

		List<ExchangeRateApprovalDetModel> exchangeRates = repo.getExchangeRatesForRoutingBank(currencyId,
				countryBranchId, countryId, applicationCountryId, routingBankId, serviceIndicatorId);
		return exchangeRates;
	}

}
