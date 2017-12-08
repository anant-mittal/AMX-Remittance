package com.amx.jax.exrateservice.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.PipsMaster;

@Transactional
public interface PipsMasterRepository extends CrudRepository<PipsMaster, BigDecimal> {

	@Query("select pips from PipsMaster pips where pips.countryBranch=?1 and pips.bankMaster is not null order by pips.currencyMaster,"
			+ "pips.bankMaster ")
	public List<PipsMaster> getPipsMasterForBranch(CountryBranch branch);
}
