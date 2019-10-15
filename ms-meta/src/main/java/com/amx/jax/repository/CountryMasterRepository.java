package com.amx.jax.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.CountryMaster;

public interface CountryMasterRepository extends JpaRepository<CountryMaster, BigDecimal> {
	public CountryMaster getCountryMasterByCountryId(BigDecimal id);
	
	@Query("select c from CountryMaster c where countryId=?1")
	public CountryMaster getCountryCodeValue(BigDecimal countryId);
}
