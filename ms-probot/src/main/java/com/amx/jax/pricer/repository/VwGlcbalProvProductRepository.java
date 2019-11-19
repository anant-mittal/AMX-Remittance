package com.amx.jax.pricer.repository;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.VwExGlcbalProvByProduct;

public interface VwGlcbalProvProductRepository extends CrudRepository<VwExGlcbalProvByProduct, BigDecimal> {

}
