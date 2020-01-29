package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.ViewExGLCBAL;

@Transactional
public interface VwExGLCBALRepository extends CrudRepository<ViewExGLCBAL, BigDecimal> {

	public List<ViewExGLCBAL> findByCurrencyCodeAndBankId(String currencyCode, BigDecimal bankId);

	public List<ViewExGLCBAL> findByCurrencyCodeAndBankIdIn(String currencyCode, List<BigDecimal> bankIds);

	public List<ViewExGLCBAL> findByCurrencyCodeInAndBankIdIn(List<String> currencyCode, List<BigDecimal> bankIds);

}
