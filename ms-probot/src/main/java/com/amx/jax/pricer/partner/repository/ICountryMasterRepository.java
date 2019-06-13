package com.amx.jax.pricer.partner.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.CountryMaster;

public interface ICountryMasterRepository extends CrudRepository<CountryMaster, Serializable> {
	
	public CountryMaster findByCountryId(BigDecimal countryId);

}
