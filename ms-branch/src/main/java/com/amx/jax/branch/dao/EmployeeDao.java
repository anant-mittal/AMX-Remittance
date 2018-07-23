package com.amx.jax.branch.dao;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.amx.jax.branch.repository.EmployeeRepository;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.Employee;



@Component
public class EmployeeDao {
	
	@Autowired
	private EmployeeRepository repo;	

	public Employee getEmployeeDetails(String civilId, BigDecimal ecNumber) {
		
		return repo.getEmployeeDetails(civilId,ecNumber,ConstantDocument.Deleted);
	}	

}
