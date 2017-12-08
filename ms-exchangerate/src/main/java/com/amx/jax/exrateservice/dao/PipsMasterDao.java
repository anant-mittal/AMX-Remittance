package com.amx.jax.exrateservice.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.PipsMaster;
import com.amx.jax.exrateservice.repository.PipsMasterRepository;

@Component
public class PipsMasterDao {

	@Autowired
	private PipsMasterRepository repo;

	public List<PipsMaster> getPipsForOnline() {
		CountryBranch onlineBranch = new CountryBranch();
		onlineBranch.setCountryBranchId(new BigDecimal(78));
		return repo.getPipsMasterForBranch(onlineBranch);
	}
}
