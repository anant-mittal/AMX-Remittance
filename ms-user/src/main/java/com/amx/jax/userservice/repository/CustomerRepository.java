package com.amx.jax.userservice.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.Customer;

@Transactional
public interface CustomerRepository extends CrudRepository<Customer, BigDecimal> {

	@Query("select c from Customer c where countryId=?1 and  identityInt =?2 and isActive='Y'")
	public List<Customer> getCustomerbyuser(BigDecimal countryId, String userId);
	
	@Query("select c from Customer c where mobile=?1 and isActive='Y'")
	public List<Customer> getCustomerByMobile(String mobile);
	
	@Query("select c from Customer c where email=?1")
	public List<Customer> getCustomerByEmailId(String emailId);
	
	public Customer findByIdentityIntAndIsActiveIsNotIn(String identityInt, String... isActive);
}
