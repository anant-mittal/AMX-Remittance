package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.amx.jax.pricer.dbmodel.OnlineMarginMarkup;

@Transactional
public interface MarginMarkupRepository extends CrudRepository<OnlineMarginMarkup, BigDecimal> {

	List<OnlineMarginMarkup> findByApplicationCountryIdAndCountryIdAndCurrencyId(BigDecimal aplCountryId,
			BigDecimal countryId, BigDecimal currencyId);

}
