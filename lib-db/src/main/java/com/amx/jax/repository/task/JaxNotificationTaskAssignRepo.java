package com.amx.jax.repository.task;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.task.JaxNotificationTaskAssign;

public interface JaxNotificationTaskAssignRepo extends CrudRepository<JaxNotificationTaskAssign, Serializable> {

	List<JaxNotificationTaskAssign> findByCountryBranchId(BigDecimal countryBranchId);
	
}
