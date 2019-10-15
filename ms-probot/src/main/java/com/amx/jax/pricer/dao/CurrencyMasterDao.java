package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.def.CacheForTenant;
import com.amx.jax.pricer.dbmodel.CurrencyMasterModel;
import com.amx.jax.pricer.repository.CurrencyMasterRepository;

@Component
public class CurrencyMasterDao {

	@Autowired
	CurrencyMasterRepository repo;

	@CacheForTenant
	public CurrencyMasterModel getByCurrencyCode(String currencyCode) {
		return repo.findByCurrencyCode(currencyCode);
	}

	@CacheForTenant
	public CurrencyMasterModel getByCurrencyId(BigDecimal currencyId) {
		return repo.findOne(currencyId);
	}

	public void updateCurrencyGroupId(CurrencyMasterModel currencyById) {
		repo.save(currencyById);
	}

	public List<CurrencyMasterModel> getCurrencyByGroupId(BigDecimal groupId) {
		return repo.getCurrencyByGroupId(groupId);
	}

	@CacheForTenant
	public List<CurrencyMasterModel> getCurrencyByIds(List<BigDecimal> currencyIds) {
		return repo.findByCurrencyIdIn(currencyIds);
	}

}
