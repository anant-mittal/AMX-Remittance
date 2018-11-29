package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.ExchangeRateApprovalDetModel;
import com.amx.jax.pricer.repository.ExchangeRateApprovalDetRepository;

@Component
public class ExchangeRateDao {

	@Autowired
	private ExchangeRateApprovalDetRepository repo;

	public List<ExchangeRateApprovalDetModel> getExchangeRates(BigDecimal currencyId, BigDecimal countryBranchId,
			BigDecimal countryId, List<BigDecimal> bankIds) {

		List<ExchangeRateApprovalDetModel> exchangeRates = repo.getExchangeRates(currencyId, countryBranchId, bankIds);
		return exchangeRates;
	}

	public List<ExchangeRateApprovalDetModel> getExchangeRatesForRoutingBank(BigDecimal currencyId,
			BigDecimal countryBranchId, BigDecimal countryId, BigDecimal applicationCountryId, BigDecimal routingBankId,
			BigDecimal serviceIndicatorId) {

		List<ExchangeRateApprovalDetModel> exchangeRates = repo.getExchangeRatesForRoutingBank(currencyId,
				countryBranchId, countryId, applicationCountryId, routingBankId, serviceIndicatorId);
		return exchangeRates;
	}

	public ExchangeRateApprovalDetModel getExchangeRateApprovalDetModelById(BigDecimal id) {
		return repo.findOne(id);
	}

	public void saveOrUpdate(ExchangeRateApprovalDetModel exchangeRateApprovalDetModel) {
		repo.save(exchangeRateApprovalDetModel);
	}

	public List<ExchangeRateApprovalDetModel> getExchangeRatesPlaceorder(BigDecimal currency, BigDecimal bankId) {
		List<ExchangeRateApprovalDetModel> exchangeRates = repo.getExchangeRatesPlaceorder(currency, bankId);
		return exchangeRates;
	}

	public List<ExchangeRateApprovalDetModel> getExchangeRatesForRoutingBanks(BigDecimal currencyId,
			BigDecimal countryBranchId, BigDecimal countryId, BigDecimal applicationCountryId,
			List<BigDecimal> routingBankIds) {

		return repo.getExchangeRatesForRoutingBanks(currencyId, countryBranchId, countryId, applicationCountryId,
				routingBankIds);
	}

}
