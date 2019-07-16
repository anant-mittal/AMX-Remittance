package com.amx.jax.repository.insurance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.insurance.InsurnaceClaimNominee;

public interface InsurnaceClaimNomineeRepository extends CrudRepository<InsurnaceClaimNominee, Serializable> {

	public List<InsurnaceClaimNominee> findByCustomerIdAndIsActive(BigDecimal customerId, String isActive);

	public List<InsurnaceClaimNominee> findByNomineeIdIn(List<BigDecimal> nomineeIds);
	
}
