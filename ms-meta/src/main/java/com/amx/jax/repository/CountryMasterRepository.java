package com.amx.jax.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.amx.jax.dbmodel.CountryMaster;

@Transactional(readOnly = true)
public interface CountryMasterRepository extends JpaRepository<CountryMaster, BigDecimal> {
	public CountryMaster getCountryMasterByCountryId(BigDecimal id);
}
