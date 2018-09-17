package com.amx.jax.exrateservice.dao;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.ExchangeRateApprovalDetModel;
import com.amx.jax.dbmodel.PipsMaster;
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

	public List<PipsMaster> getPipsForOnline(BigDecimal toCurrency) {
		logger.info("in getPipsForOnline params toCurrency:{} countryBranchId:{} ", toCurrency,
				metaData.getCountryBranchId());
		CountryBranch onlineBranch = new CountryBranch();
		onlineBranch.setCountryBranchId(metaData.getCountryBranchId());
		return repo.getPipsMasterForBranch(onlineBranch, new CurrencyMasterModel(toCurrency));
	}

	public List<PipsMaster> getPipsMasterForBranch(ExchangeRateApprovalDetModel exchangeRate, BigDecimal fcAmount) {
		CountryBranch onlineBranch = new CountryBranch();
		onlineBranch.setCountryBranchId(metaData.getCountryBranchId());
		CountryBranch countryBranch = new CountryBranch();
		countryBranch.setCountryBranchId(exchangeRate.getCountryBranchId());
		CountryMaster countryMaster = new CountryMaster();
		countryMaster.setCountryId(exchangeRate.getCountryId());

		CurrencyMasterModel currencyMaster = new CurrencyMasterModel();
		currencyMaster.setCurrencyId(exchangeRate.getCurrencyId());
		BankMasterModel bankMaster = exchangeRate.getBankMaster();
		List<PipsMaster> list = repo.getPipsMasterForBranch(countryBranch, countryMaster, bankMaster, currencyMaster,
				fcAmount);
		return list;
	}
	

	public List<PipsMaster> getPipsMaster(BigDecimal toCurrency, BigDecimal lcAmount, BigDecimal countryBranchId,
			List<BigDecimal> validBankIds) {
		return repo.getPipsMasterForOnline(toCurrency, countryBranchId, lcAmount, validBankIds);
	}
	
	public List<PipsMaster> getPipsMasterForLocalAmount(BigDecimal toCurrency, BigDecimal lcAmount, BigDecimal countryBranchId, BigDecimal bankId) {
		return repo.getPipsMasterForLocalAmount(toCurrency, countryBranchId, lcAmount, bankId);
	}
	
	public List<PipsMaster> getPipsMasterForForeignAmount(BigDecimal toCurrency, BigDecimal fcAmount,
			BigDecimal countryBranchId, BigDecimal bankId) {
		return repo.getPipsMasterForForeignAmount(toCurrency, countryBranchId, fcAmount, bankId);
	}
}
