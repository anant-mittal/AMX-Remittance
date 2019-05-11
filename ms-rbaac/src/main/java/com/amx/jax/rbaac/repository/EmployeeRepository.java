package com.amx.jax.rbaac.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.rbaac.dbmodel.CountryBranch;
import com.amx.jax.rbaac.dbmodel.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Serializable> {

	public List<Employee> findByEmployeeNumberAndCivilId(String empcode, String identity);

	//public List<Employee> findByEmployeeNumberAndCivilIdAndDeviceId(String empcode, String identity, String deviceId);
	
	public List<Employee> findByCountryBranch(CountryBranch countryBranch);
	
	public Employee findByUserNameAndPassword(String user, String pass);

	public Employee findByEmployeeNumber(String empNo);

	public Employee findByEmployeeId(BigDecimal employeeId);
	
	public List<Employee> findByCivilId(String identity);

}
