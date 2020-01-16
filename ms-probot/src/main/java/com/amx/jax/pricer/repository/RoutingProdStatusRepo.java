package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.VwExRoutingProduct;

public interface RoutingProdStatusRepo extends CrudRepository<VwExRoutingProduct, String> {

	List<VwExRoutingProduct> findByCurrencyId(BigDecimal currencyId);

	List<VwExRoutingProduct> findByCurrencyIdAndCountryId(BigDecimal currencyId, BigDecimal countryId);

	List<VwExRoutingProduct> findByCurrencyIdAndCountryIdAndBankIdIn(BigDecimal currencyId, BigDecimal countryId,
			Iterable<BigDecimal> bankIds);

}
