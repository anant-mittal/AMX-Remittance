package com.amx.jax.exrateservice.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.BankMasterMdlv1;
import com.amx.jax.dbmodel.CountryBranchMdlv1;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterMdlv1;
import com.amx.jax.dbmodel.PipsMdlv1;

@Transactional
public interface PipsMasterRepository extends CrudRepository<PipsMdlv1, BigDecimal> {

	@Query("select pips from PipsMdlv1 pips where pips.countryBranch=?1 and pips.currencyMaster=?2  order by pips.currencyMaster,"
			+ "pips.bankMaster ")
	public List<PipsMdlv1> getPipsMasterForBranch(CountryBranchMdlv1 branch, CurrencyMasterMdlv1 toCurrency);

	@Query("select pips from PipsMdlv1 pips where pips.countryBranch=?1 and pips.countryMaster=?2 and pips.bankMaster=?3 and"
			+ " pips.currencyMaster=?4 and pips.fromAmount <= ?5 and pips.toAmount >= ?5")
	public List<PipsMdlv1> getPipsMasterForBranch(CountryBranchMdlv1 countryBranch, CountryMaster countryMaster,
			BankMasterMdlv1 bankMaster, CurrencyMasterMdlv1 currencyMaster, BigDecimal fcAmount);

	@Query("select pips from PipsMdlv1 pips where pips.countryBranch=?1 and pips.countryMaster=?2 and"
			+ " pips.currencyMaster=?3 and pips.fromAmount <= ?4 and pips.toAmount >= ?4")
	public List<PipsMdlv1> getPipsMasterForBranch(CountryBranchMdlv1 countryBranch, CountryMaster countryMaster,
			CurrencyMasterMdlv1 currencyMaster, BigDecimal fcAmount);

	@Query("select pips from PipsMdlv1 pips where pips.countryMaster=?1 and"
			+ " pips.currencyMaster=?2 and pips.fromAmount <= ?3 and pips.toAmount >= ?3")
	public List<PipsMdlv1> getPipsMasterForBranch(CountryMaster countryMaster, CurrencyMasterMdlv1 currencyMaster,
			BigDecimal fcAmount);
	
	@Query(value = "select * from EX_PIPS_MASTER where CURRENCY_ID=?1 and COUNTRY_BRANCH_ID=?2 and ISACTIVE='Y'"
			+ " and ?3/DERIVED_SELL_RATE >= FROM_AMOUNT and ?3/DERIVED_SELL_RATE  <= TO_AMOUNT "
			+ " and BANK_ID in (?4) and nvl(DERIVED_SELL_RATE,0) <> 0 order by DERIVED_SELL_RATE asc", nativeQuery = true)
	public List<PipsMdlv1> getPipsMasterForOnline(BigDecimal toCurrency, BigDecimal countryBranchId,
			BigDecimal lcAmount, List<BigDecimal> validBankIds);
	
	@Query(value = "select * from EX_PIPS_MASTER where CURRENCY_ID=?1 and COUNTRY_BRANCH_ID=?2 and ISACTIVE='Y'"
			+ " and ?3/DERIVED_SELL_RATE >= FROM_AMOUNT and ?3/DERIVED_SELL_RATE  <= TO_AMOUNT and BANK_ID=?4 and nvl(DERIVED_SELL_RATE,0) <> 0 order by DERIVED_SELL_RATE asc", nativeQuery = true)
	public List<PipsMdlv1> getPipsMasterForLocalAmount(BigDecimal toCurrency, BigDecimal countryBranchId,
			BigDecimal lcAmount, BigDecimal bankId);
	
	@Query(value = "select * from EX_PIPS_MASTER where CURRENCY_ID=?1 and COUNTRY_BRANCH_ID=?2 and ISACTIVE='Y'"
			+ " and ?3 >= FROM_AMOUNT and ?3  <= TO_AMOUNT and BANK_ID=?4 order by DERIVED_SELL_RATE asc", nativeQuery = true)
	public List<PipsMdlv1> getPipsMasterForForeignAmount(BigDecimal toCurrency, BigDecimal countryBranchId,
			BigDecimal fcAmount, BigDecimal bankId);
}
