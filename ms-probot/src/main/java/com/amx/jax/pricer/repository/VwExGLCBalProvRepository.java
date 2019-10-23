package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.ViewExGLCBalProvisional;

@Transactional
public interface VwExGLCBalProvRepository extends CrudRepository<ViewExGLCBalProvisional, BigDecimal> {

	public List<ViewExGLCBalProvisional> findByCurrencyCodeAndBankIdIn(String currencyCode, List<BigDecimal> bankIds);

}
