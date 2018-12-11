package com.amx.jax.rbaac.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.EmployeeSystemsAssigned;

public interface EmployeeSystemAssignedRepository extends CrudRepository<EmployeeSystemsAssigned, Serializable> {

	public List<EmployeeSystemsAssigned> findByEmployeeIdAndCountryBranchIdAndSystemName(BigDecimal employeeId,
			BigDecimal countryBranchId, String systemName);
}
