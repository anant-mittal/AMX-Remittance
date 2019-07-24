package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.BankMasterModel;
import com.amx.jax.pricer.dbmodel.CountryBranch;
import com.amx.jax.pricer.dbmodel.CountryMaster;
import com.amx.jax.pricer.dbmodel.CurrencyMasterModel;
import com.amx.jax.pricer.dbmodel.PipsMaster;

@Transactional
public interface PipsMasterRepository extends CrudRepository<PipsMaster, BigDecimal> {

	@Query("select pips from PipsMaster pips where pips.countryBranch=?1 and pips.currencyMaster=?2  order by pips.currencyMaster,"
			+ "pips.bankMaster ")
	public List<PipsMaster> getPipsMasterForBranch(CountryBranch branch, CurrencyMasterModel toCurrency);

	@Query("select pips from PipsMaster pips where pips.countryBranch=?1 and pips.countryMaster=?2 and pips.bankMaster=?3 and"
			+ " pips.currencyMaster=?4 and pips.fromAmount <= ?5 and pips.toAmount >= ?5")
	public List<PipsMaster> getPipsMasterForBranch(CountryBranch countryBranch, CountryMaster countryMaster,
			BankMasterModel bankMaster, CurrencyMasterModel currencyMaster, BigDecimal fcAmount);

	@Query("select pips from PipsMaster pips where pips.countryBranch=?1 and pips.countryMaster=?2 and"
			+ " pips.currencyMaster=?3 and pips.fromAmount <= ?4 and pips.toAmount >= ?4")
	public List<PipsMaster> getPipsMasterForBranch(CountryBranch countryBranch, CountryMaster countryMaster,
			CurrencyMasterModel currencyMaster, BigDecimal fcAmount);

	@Query("select pips from PipsMaster pips where pips.countryMaster=?1 and"
			+ " pips.currencyMaster=?2 and pips.fromAmount <= ?3 and pips.toAmount >= ?3")
	public List<PipsMaster> getPipsMasterForBranch(CountryMaster countryMaster, CurrencyMasterModel currencyMaster,
			BigDecimal fcAmount);

	@Query(value = "select * from EX_PIPS_MASTER where CURRENCY_ID=?1 and COUNTRY_BRANCH_ID=?2 and ISACTIVE='Y'"
			+ " and ?3/DERIVED_SELL_RATE >= FROM_AMOUNT and ?3/DERIVED_SELL_RATE  <= TO_AMOUNT and COUNTRY_ID=?4"
			+ " and BANK_ID in (?5) order by DERIVED_SELL_RATE asc", nativeQuery = true)
	public List<PipsMaster> getPipsMasterForLcCurOnline(BigDecimal toCurrency, BigDecimal countryBranchId,
			BigDecimal lcAmount, BigDecimal countryId, List<BigDecimal> validBankIds);

	@Query(value = "select * from EX_PIPS_MASTER where CURRENCY_ID=?1 and COUNTRY_BRANCH_ID=?2 and ISACTIVE='Y'"
			+ " and ?3 >= FROM_AMOUNT and ?3 <= TO_AMOUNT and COUNTRY_ID=?4"
			+ " and BANK_ID in (?5) order by DERIVED_SELL_RATE asc", nativeQuery = true)
	public List<PipsMaster> getPipsMasterForFcCurOnline(BigDecimal toCurrency, BigDecimal countryBranchId,
			BigDecimal fcAmount, BigDecimal countryId, List<BigDecimal> validBankIds);

	@Query(value = "select * from EX_PIPS_MASTER where CURRENCY_ID=?1 and COUNTRY_BRANCH_ID=?2 and ISACTIVE='Y'"
			+ " and ?3/DERIVED_SELL_RATE >= FROM_AMOUNT and ?3/DERIVED_SELL_RATE  <= TO_AMOUNT and BANK_ID=?4 order by DERIVED_SELL_RATE asc", nativeQuery = true)
	public List<PipsMaster> getPipsMasterForLocalAmount(BigDecimal toCurrency, BigDecimal countryBranchId,
			BigDecimal lcAmount, BigDecimal bankId);

	@Query(value = "select * from EX_PIPS_MASTER where CURRENCY_ID=?1 and COUNTRY_BRANCH_ID=?2 and ISACTIVE='Y'"
			+ " and ?3 >= FROM_AMOUNT and ?3  <= TO_AMOUNT and BANK_ID=?4 order by DERIVED_SELL_RATE asc", nativeQuery = true)
	public List<PipsMaster> getPipsMasterForForeignAmount(BigDecimal toCurrency, BigDecimal countryBranchId,
			BigDecimal fcAmount, BigDecimal bankId);

	@Query(value = "select * from EX_PIPS_MASTER where CURRENCY_ID=?1 and COUNTRY_BRANCH_ID=?2 and ISACTIVE='Y'"
			+ " and BANK_ID in (?3) order by BANK_ID", nativeQuery = true)
	public List<PipsMaster> getPipsForFcCurAndBank(BigDecimal toCurrency, BigDecimal countryBranchId,
			List<BigDecimal> validBankIds);

	@Query(value = "select * from EX_PIPS_MASTER where COUNTRY_ID=?1 and CURRENCY_ID=?2 and COUNTRY_BRANCH_ID=?3 and "
			+ " ISACTIVE='Y' order by BANK_ID, FROM_AMOUNT", nativeQuery = true)
	public List<PipsMaster> getPipsMasterForAmountSlab(BigDecimal countryId, BigDecimal currencyId,
			BigDecimal onlineCountryBranchId);

	public PipsMaster findByPipsMasterId(BigDecimal pipsMasterId);

}
