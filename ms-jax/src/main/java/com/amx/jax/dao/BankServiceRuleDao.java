package com.amx.jax.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.BankServiceRule;
import com.amx.jax.repository.BankServiceRuleRepository;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BankServiceRuleDao {

	BankServiceRuleRepository bankServiceRuleRepo;
	
	
	public List<BankServiceRule> getBankServiceRule(BigDecimal routingBankId, BigDecimal countryId, BigDecimal currencyId,
			BigDecimal remittanceMode, BigDecimal deliveryMode) {

		return bankServiceRuleRepo.getServiceRules(routingBankId, countryId, currencyId, remittanceMode, deliveryMode);
		
	}
}
