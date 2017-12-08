package com.amx.jax.repository;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.ViewOnlineCurrency;

public interface ViewOnlineCurrencyRepository  extends CrudRepository<ViewOnlineCurrency, BigDecimal> {

}
