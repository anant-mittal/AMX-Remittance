package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.amx.jax.pricer.dbmodel.BankApplicability;
import com.amx.jax.pricer.dbmodel.BankMasterModel;

@Transactional
public interface BankApplicabilityRepository extends CrudRepository<BankApplicability, BigDecimal> {

	BankApplicability findByBankMaster(BankMasterModel bankMasterModel);

	List<BankApplicability> findByBankMasterIn(List<BankMasterModel> bankMasterModels);
}
