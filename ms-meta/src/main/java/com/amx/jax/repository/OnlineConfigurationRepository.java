package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.OnlineConfiguration;

public interface OnlineConfigurationRepository extends CrudRepository<OnlineConfiguration, BigDecimal> {

	List<OnlineConfiguration> findByappInd(String appInd);
}
