package com.amx.jax.pricer.dao;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.pricer.dbmodel.CountryBranch;
import com.amx.jax.pricer.repository.CountryBranchRepository;

@Service
public class CountryBranchDao {

	@Autowired
	CountryBranchRepository repo;

	public CountryBranch getOnlineCountryBranch() {
		return repo.findByBranchId(new BigDecimal(90));
	}

	public CountryBranch getCountryBranchByCountryBranchId(BigDecimal countryBranchId) {
		return repo.findByCountryBranchId(countryBranchId);
	}
}
