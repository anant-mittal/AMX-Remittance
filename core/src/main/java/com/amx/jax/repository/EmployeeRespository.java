package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.Employee;

@Transactional
public interface EmployeeRespository extends CrudRepository<Employee, String>{
	
	@Query("select em from Employee em where em.countryId =:applicationCountryId and em.userName =:userName and em.password =:password and em.isActive=:isActive")
	public List<Employee> checkEmployeeLogin(BigDecimal applicationCountryId,String userName,String password,String isActive);
	
	@Query(value = "SELECT * FROM FS_EMPLOYEE WHERE ROLE_ID = (Select ROLE_ID from EX_ROLE_MASTER where ROLE_CODE = '2') AND ISACTIVE = 'Y'", nativeQuery = true)
	public List<Employee> fetchEmpDriverDetails();
	
	public List<Employee> findByEmployeeId(BigDecimal employeeId);

}
