package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.emp.Employee;

public interface EmployeeRespository extends CrudRepository<Employee, String>{
	
	@Query("select em from Employee em where em.countryId =:applicationCountryId and em.userName =:userName and em.password =:password and em.isActive=:isActive")
	public List<Employee> checkEmployeeLogin(BigDecimal applicationCountryId,String userName,String password,String isActive);

}
