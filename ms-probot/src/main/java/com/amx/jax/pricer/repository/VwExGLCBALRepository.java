package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.ViewExGLCBAL;

@Transactional
public interface VwExGLCBALRepository extends CrudRepository<ViewExGLCBAL, BigDecimal> {

	List<ViewExGLCBAL> findByCurrencyCodeAndBankIdIn(String currencyCode, List<BigDecimal> bankIds);

}
