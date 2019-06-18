package com.amx.jax.pricer.partner.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.CountryMasterModel;

public interface ICountryMasterRepository extends CrudRepository<CountryMasterModel, Serializable> {
	
	public CountryMasterModel findByCountryId(BigDecimal countryId);

}
