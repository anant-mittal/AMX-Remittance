package com.amx.jax.rbaac.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.rbaac.dbmodel.CountryBranch;
import com.amx.jax.rbaac.dbmodel.FSEmployee;

public interface EmployeeRepository extends JpaRepository<FSEmployee, Serializable> {

	public List<FSEmployee> findByEmployeeNumberAndCivilId(String empcode, String identity);

	//public List<Employee> findByEmployeeNumberAndCivilIdAndDeviceId(String empcode, String identity, String deviceId);
	
	public List<FSEmployee> findByCountryBranch(CountryBranch countryBranch);
	
	public FSEmployee findByUserNameAndPassword(String user, String pass);

	public FSEmployee findByEmployeeNumber(String empNo);

	public FSEmployee findByEmployeeId(BigDecimal employeeId);
	
	public List<FSEmployee> findByCivilId(String identity);

}
