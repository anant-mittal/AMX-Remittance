package com.amx.jax.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.CardTypeViewModel;
import com.amx.jax.dbmodel.remittance.CustomerBank;
import com.amx.jax.repository.ICardTypeRepository;
import com.amx.jax.repository.remittance.CustomerBankRepository;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CardTypeDao {

	@Autowired
	ICardTypeRepository cardTypeRepo;
	
	@Autowired
	CustomerBankRepository customerBankRepository;

	public List<CardTypeViewModel> fetchCartList(BigDecimal languageId) {
		return cardTypeRepo.getCardTypeList(languageId);
	}

	public List<CustomerBank> getCustomerBanks(BigDecimal custId, BigDecimal chequeBankId) {
		// TODO Auto-generated method stub
		return customerBankRepository.getCustomerBanks(custId, chequeBankId);
	}
	
	
	
	
}
