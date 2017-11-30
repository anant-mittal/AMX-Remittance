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
}
