package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.ExchangeRateAPRDET;
import com.amx.jax.pricer.dbmodel.ExchangeRateMasterApprovalDet;
import com.amx.jax.pricer.repository.ExchangeRateApprovalDetRepository;
import com.amx.jax.pricer.repository.custom.AprDetJpaRepo;

@Component
public class ExchangeRateDao {

	@Autowired
	private ExchangeRateApprovalDetRepository repo;

	@Autowired
	private AprDetJpaRepo customRepo;

	public List<ExchangeRateMasterApprovalDet> getBranchExchangeRates(BigDecimal currencyId, BigDecimal countryBranchId,
			BigDecimal countryId, List<BigDecimal> bankIds) {

		List<ExchangeRateMasterApprovalDet> exchangeRates = repo.getExchangeRates(currencyId, countryBranchId, bankIds);
		return exchangeRates;
	}

	public List<ExchangeRateMasterApprovalDet> getBranchExchangeRatesForRoutingBank(BigDecimal currencyId,
			BigDecimal countryBranchId, BigDecimal countryId, BigDecimal applicationCountryId, BigDecimal routingBankId,
			BigDecimal serviceIndicatorId) {

		List<ExchangeRateMasterApprovalDet> exchangeRates = repo.getExchangeRatesForRoutingBank(currencyId,
				countryBranchId, countryId, applicationCountryId, routingBankId, serviceIndicatorId);
		return exchangeRates;
	}

	public ExchangeRateMasterApprovalDet getExchangeRateApprovalDetModelById(BigDecimal id) {
		return repo.findOne(id);
	}

	public void saveOrUpdate(ExchangeRateMasterApprovalDet exchangeRateMasterApprovalDet) {
		repo.save(exchangeRateMasterApprovalDet);
	}

	public List<ExchangeRateMasterApprovalDet> getExchangeRatesPlaceorder(BigDecimal currency, BigDecimal bankId) {
		List<ExchangeRateMasterApprovalDet> exchangeRates = repo.getExchangeRatesPlaceorder(currency, bankId);
		return exchangeRates;
	}

	public List<ExchangeRateMasterApprovalDet> getBranchExchangeRatesForRoutingBanks(BigDecimal currencyId,
			BigDecimal countryBranchId, BigDecimal applicationCountryId, List<BigDecimal> routingBankIds) {

		return repo.getBranchExchangeRatesForRoutingBanks(currencyId, countryBranchId, applicationCountryId,
				routingBankIds);
	}

	public List<ExchangeRateMasterApprovalDet> getBranchExchangeRatesForRoutingBanksAndServiceIds(BigDecimal currencyId,
			BigDecimal countryBranchId, BigDecimal applicationCountryId, List<BigDecimal> routingBankIds,
			List<BigDecimal> serviceIds) {

		return repo.getBranchExchangeRatesForRoutingBankAndServiceIds(currencyId, countryBranchId, applicationCountryId,
				routingBankIds, serviceIds);
	}

	public List<ExchangeRateMasterApprovalDet> getExchangeRatesForRoutingBanks(BigDecimal currencyId,
			BigDecimal countryId, BigDecimal applicationCountryId, List<BigDecimal> routingBankIds) {

		return repo.getExchangeRatesForRoutingBanks(currencyId, applicationCountryId, routingBankIds);
	}

	public List<BigDecimal> getSellRateMinForRoutingBanks(BigDecimal currencyId, BigDecimal countryId,
			BigDecimal applicationCountryId, BigDecimal routingBankId) {

		return repo.getUniqueSellRatesMinForRoutingBank(currencyId, applicationCountryId, routingBankId);
	}

	public List<ExchangeRateAPRDET> getUniqueSellRatesForRoutingBanks(BigDecimal currencyId, BigDecimal countryId,
			BigDecimal applicationCountryId, List<BigDecimal> routingBankIds, List<BigDecimal> serviceIds) {

		return repo.getSellRatesForRoutingBanks(currencyId, applicationCountryId, routingBankIds, serviceIds);
	}

	public List<ExchangeRateMasterApprovalDet> getByCurIdIn(List<BigDecimal> curIds) {
		return customRepo.findByPredicateIn(curIds);
	}

}
