package com.amx.jax.pricer.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.BankServiceRule;

public interface IBankServiceRuleRepository extends CrudRepository<BankServiceRule, Serializable> {

	@Query(value = "SELECT * FROM EX_BANK_SERVICE_RULE WHERE COUNTRY_ID=?1 AND BANK_ID=?2 AND CURRENCY_ID=?3 AND REMITTANCE_MODE_ID=?4 AND DELIVERY_MODE_ID=?5 AND ISACTIVE = 'Y'", nativeQuery = true)
	public List<BankServiceRule> fetchBankServiceRule(BigDecimal countryId, BigDecimal bankId, BigDecimal currencyId,
			BigDecimal remittanceId, BigDecimal deliveryId);

	List<BankServiceRule> findByCountryIdAndCurrencyIdAndBankIdAndRemittanceModeId(BigDecimal countryId,
			BigDecimal currencyId, BigDecimal bankId, BigDecimal remitModeId);

	List<BankServiceRule> findByCountryIdAndCurrencyIdAndBankIdAndRemittanceModeIdAndDeliveryModeId(
			BigDecimal countryId, BigDecimal currencyId, BigDecimal bankId, BigDecimal remitModeId,
			BigDecimal deliveryModeId);

	@Transactional
	@Modifying
	@Query(value = "update BankServiceRule rule set rule.isActive=?5, rule.modifiedBy=?6, rule.modifiedDate=?7 "
			+ "where rule.countryId=?1 and rule.currencyId=?2 and rule.bankId=?3 and rule.remittanceModeId=?4")
	int updateBankServiceRule(BigDecimal countryId, BigDecimal currencyId, BigDecimal bankId, BigDecimal remitModeId,
			String isActive, String modifiedBy, Date modifiedDate);

	@Transactional
	@Modifying
	@Query(value = "update BankServiceRule rule set rule.isActive=?6, rule.modifiedBy=?7, rule.modifiedDate=?8 "
			+ "where rule.countryId=?1 and rule.currencyId=?2 and rule.bankId=?3 and rule.remittanceModeId=?4 "
			+ "and rule.deliveryModeId=?5")
	int updateBankServiceRule(BigDecimal countryId, BigDecimal currencyId, BigDecimal bankId, BigDecimal remitModeId,
			BigDecimal deliveryModeId, String isActive, String modifiedBy, Date modifiedDate);

}
