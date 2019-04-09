package com.amx.jax.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.dbmodel.CountryMaster;

public interface CountryMasterRepository extends JpaRepository<CountryMaster, BigDecimal> {
	public CountryMaster getCountryMasterByCountryId(BigDecimal id);
}
