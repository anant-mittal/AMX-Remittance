package com.amx.jax.exrateservice.dao;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.BankMasterMdlv1;
import com.amx.jax.dbmodel.CountryBranchMdlv1;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterMdlv1;
import com.amx.jax.dbmodel.ExchangeRateApprovalDetModel;
import com.amx.jax.dbmodel.PipsMdlv1;
import com.amx.jax.exrateservice.repository.PipsMasterRepository;
import com.amx.jax.meta.MetaData;

@Component
public class PipsMasterDao {

	@Autowired
	private PipsMasterRepository repo;
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	private static final Logger logger = LoggerFactory.getLogger(PipsMasterDao.class);

	public List<PipsMdlv1> getPipsForOnline(BigDecimal toCurrency) {
		logger.info("in getPipsForOnline params toCurrency:{} countryBranchId:{} ", toCurrency,
				metaData.getCountryBranchId());
		CountryBranchMdlv1 onlineBranch = new CountryBranchMdlv1();
		onlineBranch.setCountryBranchId(metaData.getCountryBranchId());
		return repo.getPipsMasterForBranch(onlineBranch, new CurrencyMasterMdlv1(toCurrency));
	}

	public List<PipsMdlv1> getPipsMasterForBranch(ExchangeRateApprovalDetModel exchangeRate, BigDecimal fcAmount) {
		CountryBranchMdlv1 onlineBranch = new CountryBranchMdlv1();
		onlineBranch.setCountryBranchId(metaData.getCountryBranchId());
		CountryBranchMdlv1 countryBranch = new CountryBranchMdlv1();
		countryBranch.setCountryBranchId(exchangeRate.getCountryBranchId());
		CountryMaster countryMaster = new CountryMaster();
		countryMaster.setCountryId(exchangeRate.getCountryId());

		CurrencyMasterMdlv1 currencyMaster = new CurrencyMasterMdlv1();
		currencyMaster.setCurrencyId(exchangeRate.getCurrencyId());
		BankMasterMdlv1 bankMaster = exchangeRate.getBankMaster();
		List<PipsMdlv1> list = repo.getPipsMasterForBranch(countryBranch, countryMaster, bankMaster, currencyMaster,
				fcAmount);
		return list;
	}
	

	public List<PipsMdlv1> getPipsMaster(BigDecimal toCurrency, BigDecimal lcAmount, BigDecimal countryBranchId,
			List<BigDecimal> validBankIds) {
		return repo.getPipsMasterForOnline(toCurrency, countryBranchId, lcAmount, validBankIds);
	}
	

	public List<PipsMdlv1> getPipsMasterForLocalAmount(BigDecimal toCurrency, BigDecimal lcAmount, BigDecimal countryBranchId, BigDecimal bankId) {
		return repo.getPipsMasterForLocalAmount(toCurrency, countryBranchId, lcAmount, bankId);
	}
	
	public List<PipsMdlv1> getPipsMasterForForeignAmount(BigDecimal toCurrency, BigDecimal fcAmount,
			BigDecimal countryBranchId, BigDecimal bankId) {
		return repo.getPipsMasterForForeignAmount(toCurrency, countryBranchId, fcAmount, bankId);
	}
}
