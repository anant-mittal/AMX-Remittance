package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.ExchangeRateAPRDET;
import com.amx.jax.pricer.dbmodel.ExchangeRateApprovalDetModel;
import com.amx.jax.pricer.repository.ExchangeRateApprovalDetRepository;

@Component
public class ExchangeRateDao {

	@Autowired
	private ExchangeRateApprovalDetRepository repo;

	public List<ExchangeRateApprovalDetModel> getBranchExchangeRates(BigDecimal currencyId, BigDecimal countryBranchId,
			BigDecimal countryId, List<BigDecimal> bankIds) {

		List<ExchangeRateApprovalDetModel> exchangeRates = repo.getExchangeRates(currencyId, countryBranchId, bankIds);
		return exchangeRates;
	}

	public List<ExchangeRateApprovalDetModel> getBranchExchangeRatesForRoutingBank(BigDecimal currencyId,
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

	public List<ExchangeRateApprovalDetModel> getBranchExchangeRatesForRoutingBanks(BigDecimal currencyId,
			BigDecimal countryBranchId, BigDecimal countryId, BigDecimal applicationCountryId,
			List<BigDecimal> routingBankIds) {

		return repo.getBranchExchangeRatesForRoutingBanks(currencyId, countryBranchId, countryId, applicationCountryId,
				routingBankIds);
	}
	
	public List<ExchangeRateApprovalDetModel> getBranchExchangeRatesForRoutingBanksAndServiceIds(BigDecimal currencyId,
			BigDecimal countryBranchId, BigDecimal countryId, BigDecimal applicationCountryId,
			List<BigDecimal> routingBankIds, List<BigDecimal> serviceIds) {

		return repo.getBranchExchangeRatesForRoutingBankAndServiceIds(currencyId, countryBranchId, countryId,
				applicationCountryId, routingBankIds, serviceIds);
	}

	public List<ExchangeRateApprovalDetModel> getExchangeRatesForRoutingBanks(BigDecimal currencyId,
			BigDecimal countryId, BigDecimal applicationCountryId, List<BigDecimal> routingBankIds) {

		return repo.getExchangeRatesForRoutingBanks(currencyId, countryId, applicationCountryId, routingBankIds);
	}

	public List<BigDecimal> getSellRateMinForRoutingBanks(BigDecimal currencyId, BigDecimal countryId,
			BigDecimal applicationCountryId, BigDecimal routingBankId) {

		return repo.getUniqueSellRatesMinForRoutingBank(currencyId, countryId, applicationCountryId, routingBankId);
	}

	public List<ExchangeRateAPRDET> getUniqueSellRatesForRoutingBanks(BigDecimal currencyId, BigDecimal countryId,
			BigDecimal applicationCountryId, List<BigDecimal> routingBankIds, List<BigDecimal> serviceIds) {

		return repo.getSellRatesForRoutingBanks(currencyId, applicationCountryId, routingBankIds, serviceIds);
	}
	

}
