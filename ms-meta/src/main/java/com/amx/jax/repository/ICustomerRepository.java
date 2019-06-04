package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.Customer;

public interface ICustomerRepository extends JpaRepository<Customer, Serializable>{
	
	@Query("select c from Customer c where countryId=?1 and  identityInt =?2 and isActive='Y'")	
	public List<Customer> getCustomer(BigDecimal countryId,String userId);
	
	@Query("select c from Customer c where countryId=?1 and  companyId =?2 and customerId =?3 and isActive='Y'")	
	public List<Customer> getCustomerByCustomerId(BigDecimal countryId,BigDecimal companyId,BigDecimal customerId);
	
	@Query("select c from Customer c where countryId=?1 and  companyId =?2 and customerId =?3 and isActive='Y'")	
	public Customer getCustomerByCountryAndCompAndCustoemrId(BigDecimal countryId,BigDecimal companyId,BigDecimal customerId);
	
	@Query("select c from Customer c where customerId=?1 and isActive='Y'")	
	public Customer getCustomerDetailsByCustomerId(BigDecimal customerId);
	
	
	

}
