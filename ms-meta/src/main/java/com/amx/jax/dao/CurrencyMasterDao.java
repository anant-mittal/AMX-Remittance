package com.amx.jax.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.repository.CurrencyRepository;

@Component
public class CurrencyMasterDao {

	@Autowired
	private CurrencyRepository repo;

	public CurrencyMasterModel getCurrencyMasterById(BigDecimal id) {
		return repo.findOne(id);
	}

	public CurrencyMasterModel getCurrencyMasterByQuote(String quoteName) {
		return repo.findByquoteName(quoteName).get(0);
	}

	public Map<BigDecimal, CurrencyMasterModel> getAllCurrencyMap() {
		List<CurrencyMasterModel> list = repo.findAll();
		return list.stream().collect(Collectors.toMap(CurrencyMasterModel::getCurrencyId, c -> c));
	}

}
