package com.amx.jax.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CustomerCallDetails;

@Transactional
public interface CustomerCallDetailsRepository extends CrudRepository<CustomerCallDetails, BigDecimal> {

}
