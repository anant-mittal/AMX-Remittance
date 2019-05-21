package com.amx.jax.partner.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.CountryMasterDT;

public interface ICountryMasterRepository extends CrudRepository<CountryMasterDT, Serializable> {
	
	public CountryMasterDT findByCountryId(BigDecimal countryId);

}
