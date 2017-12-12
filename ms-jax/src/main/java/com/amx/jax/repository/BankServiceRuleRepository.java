package com.amx.jax.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.BankCharges;
import com.amx.jax.dbmodel.BankServiceRule;

@Transactional
public interface BankServiceRuleRepository extends CrudRepository<BankServiceRule, BigDecimal> {

}
