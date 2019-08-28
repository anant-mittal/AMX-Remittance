package com.amx.jax.rbaac.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.EmployeeSystemsAssigned;
import com.amx.jax.rbaac.repository.EmployeeSystemAssignedRepository;

/**
 * Dao responsible for employee and system mapping
 * 
 * @author Prashant
 *
 */
@Component
public class EmployeeSystemDao {

	@Autowired
	EmployeeSystemAssignedRepository employeeSystemsAssignedRepository;

	public void saveEmployeeSystemsAssigned(EmployeeSystemsAssigned employeeSystemsAssigned) {
		employeeSystemsAssignedRepository.save(employeeSystemsAssigned);
	}

	public List<EmployeeSystemsAssigned> findByEmployeeIdAndCountryBranchId(BigDecimal employeeId,
			BigDecimal countryBranchId, String systemName) {
		return employeeSystemsAssignedRepository.findByEmployeeIdAndCountryBranchIdAndSystemName(employeeId,
				countryBranchId, systemName);
	}

}
