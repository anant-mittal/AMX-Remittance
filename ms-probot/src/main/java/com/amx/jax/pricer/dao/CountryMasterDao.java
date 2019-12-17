package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.def.CacheForTenant;
import com.amx.jax.pricer.dbmodel.CountryMasterModel;
import com.amx.jax.pricer.repository.CountryMasterRepository;

@Component
public class CountryMasterDao {

	@Autowired
	CountryMasterRepository countryMasterRepository;

	@CacheForTenant
	public CountryMasterModel getByCountryId(BigDecimal countryId) {
		if (null == countryId) {
			return null;
		}

		return countryMasterRepository.findOne(countryId);

	}

	// @CacheForSession
	public List<CountryMasterModel> getByCountryIdIn(List<BigDecimal> countryIds) {

		if (countryIds == null || countryIds.isEmpty()) {
			return new ArrayList<CountryMasterModel>();
		}

		return countryMasterRepository.findByCountryIdIn(countryIds);
	}

}
