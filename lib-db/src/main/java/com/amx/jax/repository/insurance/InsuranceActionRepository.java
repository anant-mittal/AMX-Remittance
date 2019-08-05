package com.amx.jax.repository.insurance;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.insurance.InsuranceAction;

public interface InsuranceActionRepository extends CrudRepository<InsuranceAction, Serializable> {

	InsuranceAction findByActionId(BigDecimal actionId);
}
