package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.ViewExGLCBalProvisional;
import com.amx.jax.pricer.repository.VwExGLCBalProvRepository;

@Component
public class VwGlcbalProvProductDao {

	@Autowired
	VwExGLCBalProvRepository repo;

	public List<ViewExGLCBalProvisional> getByCurrencyCodeAndBankIdIn(String currencyCode, List<BigDecimal> bankIds) {
		return repo.findByCurrencyCodeAndBankIdIn(currencyCode, bankIds);
	}

}
