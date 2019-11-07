package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.BankMasterModel;
import com.amx.jax.pricer.repository.BankMasterRepository;

@Component
public class BankMasterDao {

	@Autowired
	BankMasterRepository bankMasterRepository;

	public BankMasterModel getBankById(BigDecimal bankId) {
		return bankMasterRepository.findOne(bankId);
	}

	public Map<BigDecimal, BankMasterModel> getBankByIdIn(List<BigDecimal> bankIds) {
		List<BankMasterModel> banks = bankMasterRepository.findByBankIdIn(bankIds);

		Map<BigDecimal, BankMasterModel> bankMap = new HashMap<BigDecimal, BankMasterModel>();

		if (banks != null && !banks.isEmpty()) {
			for (BankMasterModel bank : banks) {
				bankMap.put(bank.getBankId(), bank);
			}
		}

		return bankMap;

	}
}
