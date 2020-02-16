package com.amx.jax.dao;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.CustomerCartMaster;
import com.amx.jax.repository.remittance.CustomerCartRepository;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerCartDao {

	@Autowired
	CustomerCartRepository customerCartRepository;

	public CustomerCartMaster getCartData(BigDecimal customerId) {
		return customerCartRepository.getCartDataByCustId(customerId);
	}

	public CustomerCartMaster getCartDataByApplId(BigDecimal customerId, BigDecimal remittanceAppliId) {
		return customerCartRepository.getCartDataByApplAndCustId(customerId, remittanceAppliId);
	}
	
	
}
