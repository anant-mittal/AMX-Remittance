package com.amx.jax.repository.fx;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.fx.EmployeeDetailsView;

public interface EmployeeDetailsRepository extends CrudRepository<EmployeeDetailsView, Serializable>{
	
	public EmployeeDetailsView findByEmployeeId(BigDecimal employeeId);

}
