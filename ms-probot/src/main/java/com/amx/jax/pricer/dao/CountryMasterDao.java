package com.amx.jax.pricer.dao;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.CountryMaster;
import com.amx.jax.pricer.repository.CountryMasterRepository;

@Component
public class CountryMasterDao {

	@Autowired
	CountryMasterRepository countryMasterRepository;

	// @CacheForThis
	public CountryMaster getByCountryId(BigDecimal countryId) {
		if (null == countryId) {
			return null;
		}

		return countryMasterRepository.findOne(countryId);

	}

}
