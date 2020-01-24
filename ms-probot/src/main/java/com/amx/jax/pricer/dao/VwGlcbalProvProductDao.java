package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.VwExGlcbalProvByProduct;
import com.amx.jax.pricer.repository.VwGlcbalProvProductRepository;

@Component
public class VwGlcbalProvProductDao {

	@Autowired
	VwGlcbalProvProductRepository repo;

	public List<VwExGlcbalProvByProduct> getByCurrencyIdAndBankIdIn(BigDecimal currencyId, List<BigDecimal> bankIds) {
		return repo.findByCurrencyIdAndBankIdIn(currencyId, bankIds);
	}

}
