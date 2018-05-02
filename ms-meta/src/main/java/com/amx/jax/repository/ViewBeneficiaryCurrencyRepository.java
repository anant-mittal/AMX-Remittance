package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.bene.ViewBeneServiceCurrency;

public interface ViewBeneficiaryCurrencyRepository extends CrudRepository<ViewBeneServiceCurrency, BigDecimal> {
	
	public List<ViewBeneServiceCurrency> findByBeneCountryId(BigDecimal beneCountryId, Sort sort);
}
