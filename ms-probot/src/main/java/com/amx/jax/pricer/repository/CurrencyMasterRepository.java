package com.amx.jax.pricer.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.CurrencyMasterModel;

@Transactional
public interface CurrencyMasterRepository extends CrudRepository<CurrencyMasterModel, BigDecimal> {

	CurrencyMasterModel findByCurrencyCode(String currencyCode);

}
