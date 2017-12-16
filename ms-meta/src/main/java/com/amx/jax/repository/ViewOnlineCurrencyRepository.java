package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.ViewOnlineCurrency;

public interface ViewOnlineCurrencyRepository  extends CrudRepository<ViewOnlineCurrency, BigDecimal> {

	public List<ViewOnlineCurrency> findAll(Sort sort);
}
