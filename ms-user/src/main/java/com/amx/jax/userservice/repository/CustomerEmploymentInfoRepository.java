package com.amx.jax.userservice.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerEmploymentInfo;
import com.amx.jax.dbmodel.IncomeModel;

public interface CustomerEmploymentInfoRepository extends JpaRepository<CustomerEmploymentInfo, Serializable>{
	@Query("select c from CustomerEmploymentInfo c where c.fsCustomer =?1 and c.isActive = 'Y'")
	public CustomerEmploymentInfo getCustById(Customer customerId);
}
