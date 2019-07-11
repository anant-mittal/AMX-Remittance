package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.BankMasterModel;
import com.amx.jax.pricer.dbmodel.CountryBranch;
import com.amx.jax.pricer.dbmodel.CountryMaster;
import com.amx.jax.pricer.dbmodel.CurrencyMasterModel;
import com.amx.jax.pricer.dbmodel.ExchangeRateApprovalDetModel;
import com.amx.jax.pricer.dbmodel.PipsMaster;
import com.amx.jax.pricer.repository.PipsMasterRepository;

@Component
public class PipsMasterDao {

	@Autowired
	private PipsMasterRepository repo;

	private static final Logger logger = LoggerFactory.getLogger(PipsMasterDao.class);

	public List<PipsMaster> getPipsForOnline(BigDecimal toCurrency, BigDecimal countryBranchId) {
		logger.info("in getPipsForOnline params toCurrency:{} countryBranchId:{} ", toCurrency, countryBranchId);
		CountryBranch onlineBranch = new CountryBranch();
		onlineBranch.setCountryBranchId(countryBranchId);
		return repo.getPipsMasterForBranch(onlineBranch, new CurrencyMasterModel(toCurrency));
	}

	public List<PipsMaster> getPipsMasterForBranch(ExchangeRateApprovalDetModel exchangeRate, BigDecimal fcAmount,
			BigDecimal countryBranchId) {
		CountryBranch onlineBranch = new CountryBranch();
		onlineBranch.setCountryBranchId(exchangeRate.getCountryBranchId());
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

	public List<PipsMaster> getPipsMasterForLcCur(BigDecimal toCurrency, BigDecimal lcAmount,
			BigDecimal countryBranchId, BigDecimal countryId, List<BigDecimal> validBankIds) {
		return repo.getPipsMasterForLcCurOnline(toCurrency, countryBranchId, lcAmount, countryId, validBankIds);
	}

	public List<PipsMaster> getPipsMasterForFcCur(BigDecimal toCurrency, BigDecimal fcAmount,
			BigDecimal countryBranchId, BigDecimal countryId, List<BigDecimal> validBankIds) {
		return repo.getPipsMasterForFcCurOnline(toCurrency, countryBranchId, fcAmount, countryId, validBankIds);
	}

	public List<PipsMaster> getPipsMasterForLocalAmount(BigDecimal toCurrency, BigDecimal lcAmount,
			BigDecimal countryBranchId, BigDecimal bankId) {
		return repo.getPipsMasterForLocalAmount(toCurrency, countryBranchId, lcAmount, bankId);
	}

	public List<PipsMaster> getPipsMasterForForeignAmount(BigDecimal toCurrency, BigDecimal fcAmount,
			BigDecimal countryBranchId, BigDecimal bankId) {
		return repo.getPipsMasterForForeignAmount(toCurrency, countryBranchId, fcAmount, bankId);
	}

	public List<PipsMaster> getPipsForFcCurAndBank(BigDecimal toCurrency, BigDecimal countryBranchId,
			List<BigDecimal> validBankIds) {
		return repo.getPipsForFcCurAndBank(toCurrency, countryBranchId, validBankIds);
	}

	public List<PipsMaster> getAmountSlab(BigDecimal countryId, BigDecimal currencyId,
			BigDecimal onlineCountryBranchId) {
		return repo.getPipsMasterForAmountSlab(countryId, currencyId, onlineCountryBranchId);
	}

	public PipsMaster getPipsById(BigDecimal id) {
		return repo.findByPipsMasterId(id);
	}

	public void savePipsForDiscount(List<PipsMaster> pipslDiscounts) {
		repo.save(pipslDiscounts);
	}

}
