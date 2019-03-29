package com.amx.jax.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CustomerContactVerification;

@Transactional
public interface CustomerContactVerificationRepository extends CrudRepository<CustomerContactVerification, BigDecimal> {

	@Query("select c from CustomerContactVerification c where id=?1")
	public CustomerContactVerification getCustomerData(String id);

}
