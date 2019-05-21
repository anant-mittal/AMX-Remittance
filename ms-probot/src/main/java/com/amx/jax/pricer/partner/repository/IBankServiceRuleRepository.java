package com.amx.jax.pricer.partner.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.partner.dbmodel.BankServiceRule;

public interface IBankServiceRuleRepository extends CrudRepository<BankServiceRule, Serializable> {
	
	@Query(value = "SELECT * FROM EX_BANK_SERVICE_RULE WHERE COUNTRY_ID=?1 AND BANK_ID=?2 AND CURRENCY_ID=?3 AND REMITTANCE_MODE_ID=?4 AND DELIVERY_MODE_ID=?5 AND ISACTIVE = 'Y'", nativeQuery = true)
	public List<BankServiceRule> fetchBankServiceRule(BigDecimal countryId,BigDecimal bankId,BigDecimal currencyId,BigDecimal remittanceId,BigDecimal deliveryId);

}
