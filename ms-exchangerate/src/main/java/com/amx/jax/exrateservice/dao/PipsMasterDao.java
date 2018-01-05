package com.amx.jax.exrateservice.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.ExchangeRateApprovalDetModel;
import com.amx.jax.dbmodel.PipsMaster;
import com.amx.jax.exrateservice.repository.PipsMasterRepository;

@Component
public class PipsMasterDao {

	@Autowired
	private PipsMasterRepository repo;

	public List<PipsMaster> getPipsForOnline(BigDecimal toCurrency) {
		CountryBranch onlineBranch = new CountryBranch();
		onlineBranch.setCountryBranchId(new BigDecimal(78));
		return repo.getPipsMasterForBranch(onlineBranch, new CurrencyMasterModel(toCurrency));
	}

	public List<PipsMaster> getPipsMasterForBranch(ExchangeRateApprovalDetModel exchangeRate, BigDecimal fcAmount) {
		CountryBranch onlineBranch = new CountryBranch();
		onlineBranch.setCountryBranchId(new BigDecimal(78));
		CountryBranch countryBranch = new CountryBranch();
		countryBranch.setCountryBranchId(exchangeRate.getCountryBranchId());
		CountryMaster countryMaster = new CountryMaster();
		countryMaster.setCountryId(exchangeRate.getCountryId());

		CurrencyMasterModel currencyMaster = new CurrencyMasterModel();
		currencyMaster.setCurrencyId(exchangeRate.getCurrencyId());
		BankMasterModel bankMaster = exchangeRate.getBankMaster();
		List<PipsMaster> list = repo.getPipsMasterForBranch(countryBranch, countryMaster, bankMaster, currencyMaster,
				fcAmount);
		// discount without bank and countrybranch
		if (list == null || list.isEmpty()) {
			list = repo.getPipsMasterForBranch(countryBranch, countryMaster, currencyMaster, fcAmount);
		}
		if (list == null || list.isEmpty()) {
			list = repo.getPipsMasterForBranch(countryMaster, currencyMaster, fcAmount);
		}
		return list;
	}
}
