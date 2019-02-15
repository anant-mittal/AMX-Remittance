package com.amx.jax.pricer.repository;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.CustomerExtended;

public interface CustomerExtendedRepository extends CrudRepository<CustomerExtended, BigDecimal> {

	CustomerExtended findByCustomerId(BigDecimal customerId);

}
