package com.amx.jax.pricer.repository;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.amx.jax.pricer.dbmodel.CountryMasterModel;

@Transactional
public interface CountryMasterRepository extends CrudRepository<CountryMasterModel, BigDecimal> {

}
