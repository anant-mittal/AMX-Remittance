package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	public Map<BigDecimal, CountryBranch> getByCountryBranchIds(List<BigDecimal> countryBranchIds) {
		List<CountryBranch> cbList = repo.findByCountryBranchIdIn(countryBranchIds);

		Map<BigDecimal, CountryBranch> cbMap;

		if (cbList != null && !cbList.isEmpty()) {
			cbMap = cbList.stream().collect(Collectors.toMap(CountryBranch::getCountryBranchId, cb -> cb));
		} else {
			cbMap = new HashMap<BigDecimal, CountryBranch>();
		}

		return cbMap;
	}
}
