package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.amx.jax.model.Customer;

public interface ICustomerRepository extends JpaRepository<Customer, Serializable>{
	
	@Query("select c from Customer c where countryId=?1 and  identityInt =?2 and isActive='Y'")	
	public List<Customer> getCustomer(BigDecimal countryId,String userId);

	
	
}
