package com.amx.jax.exrateservice.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.PipsMaster;

@Transactional
public interface PipsMasterRepository extends CrudRepository<PipsMaster, BigDecimal> {

	@Query("select pips from PipsMaster pips where pips.countryBranch=?1 and pips.bankMaster is not null order by pips.currencyMaster,"
			+ "pips.bankMaster ")
	public List<PipsMaster> getPipsMasterForBranch(CountryBranch branch);

	@Query("select pips from PipsMaster pips where pips.countryBranch=?1 and pips.countryMaster=?2 and pips.bankMaster=?3 and"
			+ " pips.currencyMaster=?4 and pips.fromAmount > ?5 and pips.toAmount < ?5")
	public List<PipsMaster> getPipsMasterForBranch(CountryBranch countryBranch, CountryMaster countryMaster,
			BankMasterModel bankMaster, CurrencyMasterModel currencyMaster, BigDecimal fcAmount);
}
