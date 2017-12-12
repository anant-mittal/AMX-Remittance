package com.amx.jax.dao;

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
	
//	public BankServiceRule getBankServiceRule() {
//		
//	}
}
