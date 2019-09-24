package com.amx.jax.pricer.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.BankApplicability;
import com.amx.jax.pricer.dbmodel.BankMasterModel;
import com.amx.jax.pricer.repository.BankApplicabilityRepository;

@Component
public class BankApplicabilityDao {

	@Autowired
	BankApplicabilityRepository repository;

	public BankApplicability findByBankMaster(BankMasterModel bankMasterModel) {
		return repository.findByBankMaster(bankMasterModel);
	}

	public List<BankApplicability> findByBankMasterIds(List<BankMasterModel> bankMasterModels) {

		return repository.findByBankMasterIn(bankMasterModels);
	}

}
