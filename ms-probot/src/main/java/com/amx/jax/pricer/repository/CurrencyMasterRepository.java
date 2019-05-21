package com.amx.jax.pricer.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.CurrencyMasterModel;

public interface CurrencyMasterRepository extends CrudRepository<CurrencyMasterModel, Serializable> {

	public List<CurrencyMasterModel> findByIsoCurrencyCodeAndIsactive(String currencyCode,String isActive);
	
	public CurrencyMasterModel findByCurrencyCode(String currencyCode);

}
