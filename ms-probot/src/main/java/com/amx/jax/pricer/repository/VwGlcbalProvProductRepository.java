package com.amx.jax.pricer.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.VwExGlcbalProvByProduct;

public interface VwGlcbalProvProductRepository extends CrudRepository<VwExGlcbalProvByProduct, Serializable> {

	List<VwExGlcbalProvByProduct> findByCurrencyIdAndBankIdIn(BigDecimal currencyId, Iterable<BigDecimal> bankIds);

}
