package com.amx.jax.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.repository.CountryBranchRepository;

@Service
public class CountryBranchService {

	@Autowired
	CountryBranchRepository repo;

	public CountryBranch getOnlineCountryBranch() {
		return repo.findByBranchId(new BigDecimal(90));
	}

}
