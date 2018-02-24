package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dao.BankDao;
import com.amx.jax.dbmodel.BankBranchView;
import com.amx.jax.dbmodel.remittance.AdditionalBankDetailsView;
import com.amx.jax.repository.IAdditionalBankDetailsDao;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BankService {

	@Autowired
	BankDao bankDao;

	@Autowired
	IAdditionalBankDetailsDao bankDetailsDao;

	public String getBranchSwiftCode(BigDecimal bankId, BigDecimal bankBranchId) {
		BankBranchView branch = bankDao.getBankBranchById(bankId, bankBranchId);
		String swift = null;
		if (branch != null) {
			swift = branch.getSwift();
		}
		return swift;
	}

	public AdditionalBankDetailsView getAdditionalBankDetail(BigDecimal srlId, BigDecimal currencyId, BigDecimal bankId,
			BigDecimal remittanceModeId, BigDecimal deleveryModeId) {

		List<AdditionalBankDetailsView> additionalBankDetails = bankDetailsDao.getAdditionalBankDetails(srlId,
				currencyId, bankId, remittanceModeId, deleveryModeId);
		return additionalBankDetails.get(0);
	}
}
