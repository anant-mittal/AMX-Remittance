package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.BankServiceRule;

@Transactional
public interface BankServiceRuleRepository extends CrudRepository<BankServiceRule, BigDecimal> {

	@Query(value = "select * from EX_BANK_SERVICE_RULE where BANK_ID=?1 and COUNTRY_ID=?2 and CURRENCY_ID=?3  and  REMITTANCE_MODE_ID=?4"
			+ " and DELIVERY_MODE_ID=?5 and ISACTIVE='Y'", nativeQuery = true)
	List<BankServiceRule> getServiceRules(BigDecimal routingBankId, BigDecimal countryId, BigDecimal currencyId,
			BigDecimal remittanceMode, BigDecimal deliveryMode);

	List<BankServiceRule> findByCountryId(BigDecimal countryId);
}
