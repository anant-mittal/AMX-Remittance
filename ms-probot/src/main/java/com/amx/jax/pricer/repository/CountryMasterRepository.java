package com.amx.jax.pricer.repository;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.amx.jax.pricer.dbmodel.CountryMaster;

@Transactional
public interface CountryMasterRepository extends CrudRepository<CountryMaster, BigDecimal> {

}
