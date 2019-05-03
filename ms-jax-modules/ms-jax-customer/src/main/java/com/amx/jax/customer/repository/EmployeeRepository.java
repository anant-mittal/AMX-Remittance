package com.amx.jax.customer.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.Employee;

@Transactional
public interface EmployeeRepository extends CrudRepository<Employee, BigDecimal>{

	@Query("select e from Employee e where civilId=?1 and  employeeId =?2 and isActive !=?3")
	Employee getEmployeeDetails(String civilId, BigDecimal ecNumber, String deleted);

	Employee getEmployeeByCivilId(String civilId);

	Employee findByEmployeeId(BigDecimal empoloyeeId);
	
	@Query("select e from Employee e where employeeId=?1 and isActive ='Y' ")
	Employee getEmployeeName(BigDecimal employeeId);
	

}
