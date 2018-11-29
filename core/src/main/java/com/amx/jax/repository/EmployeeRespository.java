package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.Employee;

public interface EmployeeRespository extends CrudRepository<Employee, String>{
	
	@Query("select em from Employee em where em.countryId =:applicationCountryId and em.userName =:userName and em.password =:password and em.isActive=:isActive")
	public List<Employee> checkEmployeeLogin(BigDecimal applicationCountryId,String userName,String password,String isActive);
	
	@Query("select em from Employee em where em.fsRoleMaster = (Select rm.roleId from RoleMaster rm where rm.roleCode='2') and em.isActive='Y'")
	public List<Employee> fetchEmpDriverDetails();
	
	public List<Employee> findByEmployeeId(BigDecimal employeeId);

}
