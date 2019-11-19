package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.VwExRoutingProductStatus;

public interface RoutingProdStatusRepo extends CrudRepository<VwExRoutingProductStatus, String> {

	List<VwExRoutingProductStatus> findByCurrencyId(BigDecimal currencyId);

	List<VwExRoutingProductStatus> findByCurrencyIdAndCountryId(BigDecimal currencyId, BigDecimal countryId);

	List<VwExRoutingProductStatus> findByBankIdIn(Iterable<BigDecimal> bankIds);

}
