package com.amx.jax.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.JaxConfig;

@Transactional
public interface JaxConfigRepository extends CrudRepository<JaxConfig, BigDecimal> {

	public JaxConfig findByType(String type);
}
