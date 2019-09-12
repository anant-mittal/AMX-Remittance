package com.amx.jax.pricer.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.EmployeeDetailView;

public interface EmployeeDetailsViewRepository extends CrudRepository<EmployeeDetailView, Serializable>{
	
	public EmployeeDetailView findByEmployeeId(BigDecimal employeeId);

}
