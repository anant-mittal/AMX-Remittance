package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amx.jax.pricer.dbmodel.BankServiceRule;
import com.amx.jax.pricer.repository.IBankServiceRuleRepository;

@Component
public class BankServiceRuleDao {

	@Autowired
	IBankServiceRuleRepository repo;

	public List<BankServiceRule> getByCountryIdAndCurrencyIdAndBankIdAndRemittanceModeId(BigDecimal countryId,
			BigDecimal currencyId, BigDecimal bankId, BigDecimal remitModeId) {
		return repo.findByCountryIdAndCurrencyIdAndBankIdAndRemittanceModeId(countryId, currencyId, bankId,
				remitModeId);
	}

	public List<BankServiceRule> getByCountryIdAndCurrencyIdAndBankIdAndRemittanceModeIdAndDeliveryModeId(
			BigDecimal countryId, BigDecimal currencyId, BigDecimal bankId, BigDecimal remitModeId,
			BigDecimal deliveryModeId) {
		return repo.findByCountryIdAndCurrencyIdAndBankIdAndRemittanceModeIdAndDeliveryModeId(countryId, currencyId,
				bankId, remitModeId, deliveryModeId);
	}

	@Modifying
	@Transactional
	public BankServiceRule updateBankServiceRule(BankServiceRule rule) {
		return repo.save(rule);
	}

	@Modifying
	@Transactional
	public int updateBankServiceRule(BigDecimal countryId, BigDecimal currencyId, BigDecimal bankId,
			BigDecimal remitModeId, String isActive) {
		return repo.updateBankServiceRule(countryId, currencyId, bankId, remitModeId, isActive);

	}

	@Transactional
	@Modifying
	public int updateBankServiceRule(BigDecimal countryId, BigDecimal currencyId, BigDecimal bankId,
			BigDecimal remitModeId, BigDecimal deliveryModeId, String isActive) {
		return repo.updateBankServiceRule(countryId, currencyId, bankId, remitModeId, deliveryModeId, isActive);
	}

}
