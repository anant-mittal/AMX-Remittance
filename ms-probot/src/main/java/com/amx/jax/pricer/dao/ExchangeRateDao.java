package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.def.CacheForSession;
import com.amx.jax.pricer.dbmodel.ExchangeRateAPRDET;
import com.amx.jax.pricer.dbmodel.ExchangeRateApprovalDetModelAlt;
import com.amx.jax.pricer.repository.ExchangeRateApprovalDetRepository;

@Component
public class ExchangeRateDao {

	@Autowired
	private ExchangeRateApprovalDetRepository repo;

	public List<ExchangeRateApprovalDetModelAlt> getBranchExchangeRates(BigDecimal currencyId, BigDecimal countryBranchId,
			BigDecimal countryId, List<BigDecimal> bankIds) {

		List<ExchangeRateApprovalDetModelAlt> exchangeRates = repo.getExchangeRates(currencyId, countryBranchId, bankIds);
		return exchangeRates;
	}

	public List<ExchangeRateApprovalDetModelAlt> getBranchExchangeRatesForRoutingBank(BigDecimal currencyId,
			BigDecimal countryBranchId, BigDecimal countryId, BigDecimal applicationCountryId, BigDecimal routingBankId,
			BigDecimal serviceIndicatorId) {

		List<ExchangeRateApprovalDetModelAlt> exchangeRates = repo.getExchangeRatesForRoutingBank(currencyId,
				countryBranchId, countryId, applicationCountryId, routingBankId, serviceIndicatorId);
		return exchangeRates;
	}

	public ExchangeRateApprovalDetModelAlt getExchangeRateApprovalDetModelById(BigDecimal id) {
		return repo.findOne(id);
	}

	public void saveOrUpdate(ExchangeRateApprovalDetModelAlt exchangeRateApprovalDetModelAlt) {
		repo.save(exchangeRateApprovalDetModelAlt);
	}

	public List<ExchangeRateApprovalDetModelAlt> getExchangeRatesPlaceorder(BigDecimal currency, BigDecimal bankId) {
		List<ExchangeRateApprovalDetModelAlt> exchangeRates = repo.getExchangeRatesPlaceorder(currency, bankId);
		return exchangeRates;
	}

	public List<ExchangeRateApprovalDetModelAlt> getBranchExchangeRatesForRoutingBanks(BigDecimal currencyId,
			BigDecimal countryBranchId, BigDecimal applicationCountryId, List<BigDecimal> routingBankIds) {

		return repo.getBranchExchangeRatesForRoutingBanks(currencyId, countryBranchId, applicationCountryId,
				routingBankIds);
	}

	public List<ExchangeRateApprovalDetModelAlt> getBranchExchangeRatesForRoutingBanksAndServiceIds(BigDecimal currencyId,
			BigDecimal countryBranchId, BigDecimal applicationCountryId, List<BigDecimal> routingBankIds,
			List<BigDecimal> serviceIds) {

		return repo.getBranchExchangeRatesForRoutingBankAndServiceIds(currencyId, countryBranchId, applicationCountryId,
				routingBankIds, serviceIds);
	}

	public List<ExchangeRateApprovalDetModelAlt> getExchangeRatesForRoutingBanks(BigDecimal currencyId,
			BigDecimal countryId, BigDecimal applicationCountryId, List<BigDecimal> routingBankIds) {

		return repo.getExchangeRatesForRoutingBanks(currencyId, applicationCountryId, routingBankIds);
	}

	public List<BigDecimal> getSellRateMinForRoutingBanks(BigDecimal currencyId, BigDecimal countryId,
			BigDecimal applicationCountryId, BigDecimal routingBankId) {

		return repo.getUniqueSellRatesMinForRoutingBank(currencyId, applicationCountryId, routingBankId);
	}

	@CacheForSession
	public List<ExchangeRateAPRDET> getUniqueSellRatesForRoutingBanks(BigDecimal currencyId, BigDecimal countryId,
			BigDecimal applicationCountryId, List<BigDecimal> routingBankIds, List<BigDecimal> serviceIds) {

		return repo.getSellRatesForRoutingBanks(currencyId, applicationCountryId, routingBankIds, serviceIds);
	}

}
