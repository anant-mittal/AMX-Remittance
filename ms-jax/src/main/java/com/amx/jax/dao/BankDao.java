package com.amx.jax.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.BankBranchView;
import com.amx.jax.dbmodel.BankServiceRule;
import com.amx.jax.repository.BankServiceRuleRepository;
import com.amx.jax.repository.IBankBranchView;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BankDao {

	@Autowired
	BankServiceRuleRepository bankServiceRuleRepo;

	@Autowired
	IBankBranchView bankBranchRepo;

	public List<BankServiceRule> getBankServiceRule(BigDecimal routingBankId, BigDecimal countryId,
			BigDecimal currencyId, BigDecimal remittanceMode, BigDecimal deliveryMode) {
		return bankServiceRuleRepo.getServiceRules(routingBankId, countryId, currencyId, remittanceMode, deliveryMode);

	}

	public BankBranchView getBankBranchById(BigDecimal bankId, BigDecimal bankBranchId) {
		List<BankBranchView> branches = bankBranchRepo.getBankBranch(bankId, bankBranchId);
		BankBranchView branch= null;
		if(branches != null && !branches.isEmpty()) {
			branch =  branches.get(0);
		}
		return branch;
	}
}
