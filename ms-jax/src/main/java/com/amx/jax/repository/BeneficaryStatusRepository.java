package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.bene.BeneficaryStatus;

public interface BeneficaryStatusRepository extends CrudRepository<BeneficaryStatus, BigDecimal> {

	public BeneficaryStatus findByBeneficaryStatusName(String beneficaryStatusName);

	public List<BeneficaryStatus> findByIsActive(String isActive);
}
