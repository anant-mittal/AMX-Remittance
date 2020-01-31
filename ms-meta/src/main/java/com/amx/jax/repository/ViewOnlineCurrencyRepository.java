package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.ViewOnlineCurrency;

public interface ViewOnlineCurrencyRepository  extends CrudRepository<ViewOnlineCurrency, BigDecimal> {

	public List<ViewOnlineCurrency> findAll(Sort sort);
	
	@Query("select a from ViewOnlineCurrency a where a.isActive=:isActive")
	public List<ViewOnlineCurrency> findByIsActive(Sort sort,@Param("isActive")String isActive);
	
	
}
