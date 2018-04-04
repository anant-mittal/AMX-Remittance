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
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.bene.BankAccountLength;
import com.amx.jax.dbmodel.remittance.AdditionalBankDetailsView;
import com.amx.jax.repository.BankMasterRepository;
import com.amx.jax.repository.IAdditionalBankDetailsDao;
import com.amx.jax.repository.IBankAccountLengthDao;
import com.amx.jax.repository.IBankBranchView;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BankService {

	@Autowired
	BankDao bankDao;

	@Autowired
	IAdditionalBankDetailsDao bankDetailsDao;
	
	@Autowired
	IBankAccountLengthDao bankAccountLengthDao;
	
	@Autowired
	IBankBranchView bankBranchViewRepo;
	
	@Autowired
	BankMasterRepository bankMasterRepository;

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
	
	public List<BankAccountLength> getBankAccountLength(BigDecimal bankId) {
		return bankAccountLengthDao.getBankAccountLength(bankId);
	}
	
	public BankBranchView getBankBranchView(BigDecimal bankId, BigDecimal bankBranchId) {
		BankBranchView bankBranch = null;
		List<BankBranchView> bankBranchList = bankBranchViewRepo.getBankBranch(bankId, bankBranchId);
		if (bankBranchList != null && !bankBranchList.isEmpty()) {
			bankBranch = bankBranchList.get(0);
		}
		return bankBranch;
	}
	
	public BankMasterModel getBankById(BigDecimal bankId) {
		return bankMasterRepository.findOne(bankId);
	}
}
