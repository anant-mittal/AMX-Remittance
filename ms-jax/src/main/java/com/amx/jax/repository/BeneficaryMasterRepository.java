package com.amx.jax.repository;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.bene.BeneficaryMaster;

public interface BeneficaryMasterRepository extends CrudRepository<BeneficaryMaster, BigDecimal> {

}
