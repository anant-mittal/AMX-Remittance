package com.amx.jax.repository;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.bene.BeneficaryAccount;

public interface BeneficaryAccountRepository extends CrudRepository<BeneficaryAccount, BigDecimal> {

}
