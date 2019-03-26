package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.EmployeeDetails;

public interface CustomerEmployeeDetailsRepository extends JpaRepository< EmployeeDetails, Serializable>{

	@Query("select e from EmployeeDetails e where fsCustomer=?1 ")
	public EmployeeDetails getCustomerEmploymentData(Customer customerId);
}
