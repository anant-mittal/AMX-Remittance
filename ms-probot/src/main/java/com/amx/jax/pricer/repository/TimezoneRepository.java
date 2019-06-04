package com.amx.jax.pricer.repository;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.TimezoneMasterModel;

public interface TimezoneRepository extends CrudRepository<TimezoneMasterModel, BigDecimal> {

	public TimezoneMasterModel findByTimezone(String tz);

	public TimezoneMasterModel findByCountryId(BigDecimal countryId);
}
