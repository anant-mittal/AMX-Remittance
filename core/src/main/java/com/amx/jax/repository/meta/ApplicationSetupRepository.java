package com.amx.jax.repository.meta;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.ApplicationSetup;

public interface ApplicationSetupRepository extends CrudRepository<ApplicationSetup, BigDecimal> {

	List<ApplicationSetup> findByApplicationCountryId(BigDecimal appliationCountryId);
}
