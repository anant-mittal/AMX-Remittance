package com.amx.jax.repository.customer;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.IdentityTypeMaster;

public interface IdentityTypeMasterRepository extends CrudRepository<IdentityTypeMaster, Serializable> {

	public IdentityTypeMaster findBybusinessComponentIdAndIsActive(BigDecimal businessComponentId, String isActive);
}
