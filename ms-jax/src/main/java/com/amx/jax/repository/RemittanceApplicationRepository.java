package com.amx.jax.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.RemittanceApplication;

@Transactional
public interface RemittanceApplicationRepository extends CrudRepository<RemittanceApplication, BigDecimal> {

}
