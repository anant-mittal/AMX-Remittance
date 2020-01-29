package com.amx.jax.complaince;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.CountryMaster;

public interface CountryMasterRepository extends JpaRepository<CountryMaster, BigDecimal> {
	public CountryMaster getCountryMasterByCountryId(BigDecimal id);
	
	@Query("select c from CountryMaster c where countryId=?1")
	public List<CountryMaster> getCountryAlpha2Code(BigDecimal countryId);
}
