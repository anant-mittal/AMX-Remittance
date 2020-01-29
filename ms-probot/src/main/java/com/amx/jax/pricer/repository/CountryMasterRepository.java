package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.amx.jax.pricer.dbmodel.CountryMasterModel;

@Transactional
public interface CountryMasterRepository extends CrudRepository<CountryMasterModel, BigDecimal> {

	public CountryMasterModel findByCountryId(BigDecimal countryId);

	public List<CountryMasterModel> findByCountryIdIn(List<BigDecimal> countryIds);

}
