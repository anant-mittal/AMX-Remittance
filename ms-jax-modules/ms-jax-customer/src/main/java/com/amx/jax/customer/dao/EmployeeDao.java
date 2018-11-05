package com.amx.jax.customer.dao;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.customer.repository.EmployeeRepository;
import com.amx.jax.dbmodel.Employee;
import com.amx.jax.dbmodel.employee.AmgEmployee;
import com.amx.jax.repository.employee.AmgEmployeeRepository;

@Component
public class EmployeeDao {
	
	@Autowired
	private EmployeeRepository repo;	

	@Autowired
	AmgEmployeeRepository amgEmployeeRepository;

	public Employee getEmployeeDetails(String civilId, BigDecimal ecNumber) {
		
		return repo.getEmployeeDetails(civilId,ecNumber,ConstantDocument.Deleted);
	}	

	public Employee getEmployeeDetails(BigDecimal employeeId) {
		
		return repo.findByEmployeeId(employeeId);
	}	
	/**
	 * @param civilId
	 * @return AmgEmployee object which belong to table which is superset of all
	 *         employee
	 * 
	 */
	public AmgEmployee getAmgEmployee(String civilId) {
		return amgEmployeeRepository.findByCivilIdAndEmployeeInd(civilId, "AMIEC");
	}

	public boolean isAmgEmployee(String civilId) {
		if (getAmgEmployee(civilId) != null) {
			return true;
		} else {
			return false;
		}
	}
}
