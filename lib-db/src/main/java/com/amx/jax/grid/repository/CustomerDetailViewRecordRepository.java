package com.amx.jax.grid.repository;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.grid.views.CustomerDetailViewRecord;

public interface CustomerDetailViewRecordRepository extends CrudRepository<CustomerDetailViewRecord, BigDecimal> {

}