package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.Customer;

public interface ICustomerRepository extends JpaRepository<Customer, Serializable>{
	
	@Query("select c from Customer c where countryId=?1 and  identityInt =?2 and isActive='Y'")	
	public List<Customer> getCustomer(BigDecimal countryId,String userId);

	
	
	@Query("select c from Customer c where countryId=?1 and  companyId =?2 and customerId =?3 and isActive='Y'")	
	public List<Customer> getCustomerByCustomerId(BigDecimal countryId,BigDecimal companyId,BigDecimal customerId);
	
	
	@Modifying
	@Query("update Customer c set c.loyaltyPoints = ?1 where c.companyId =?2 and c.countryId=?3 and c.customerId = ?4")
	public void updateLoyaltyPoints(BigDecimal loyaltyPoint,BigDecimal companyId,BigDecimal countryId, BigDecimal customerId);
	
}
