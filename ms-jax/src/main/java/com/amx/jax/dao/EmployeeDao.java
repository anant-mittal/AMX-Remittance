package com.amx.jax.dao;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.Employee;
import com.amx.jax.dbmodel.employee.AmgEmployee;
import com.amx.jax.repository.employee.AmgEmployeeRepository;

@Component
public class EmployeeDao {

	@Autowired
	AmgEmployeeRepository amgEmployeeRepository;

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
