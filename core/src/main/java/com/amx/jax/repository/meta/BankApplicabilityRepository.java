package com.amx.jax.repository.meta;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.BankMasterMdlv1;
import com.amx.jax.dbmodel.treasury.BankApplicability;

public interface BankApplicabilityRepository extends CrudRepository<BankApplicability, BigDecimal> {

	BankApplicability findByBankMaster(BankMasterMdlv1 bankMasterModel);
}
