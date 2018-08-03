package com.amx.jax.auth.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.auth.dbmodel.Employee;

public interface ILoginRepository extends JpaRepository<Employee, Serializable>{
	
	public Employee findByEmployeeNumberAndCivilId(String empcode,String identity);
	
	public Employee findByUserNameAndPassword(String user,String pass);
	
	public Employee findByEmployeeNumber(String empNo);
	
}
