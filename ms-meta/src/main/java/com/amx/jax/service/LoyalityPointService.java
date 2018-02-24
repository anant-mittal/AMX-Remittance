package com.amx.jax.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.remittance.VwLoyalityEncash;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.VwLoyalityEncashRepository;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)

public class LoyalityPointService {

	@Autowired
	VwLoyalityEncashRepository repo;

	@Autowired
	MetaData meta;

	public VwLoyalityEncash getVwLoyalityEncash() {
		Iterable<VwLoyalityEncash> loyalityPointMaster = repo.findAll();
		VwLoyalityEncash view = loyalityPointMaster.iterator().next();
		return view;
	}

	public BigDecimal getTodaysLoyalityPointsEncashed() {
		return repo.getTodaysLoyalityPointsEncashed(meta.getCustomerId());
	}
}
