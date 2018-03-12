package com.amx.jax.userservice.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CustomerVerification;

@Transactional
public interface CustomerVerificationRepository extends CrudRepository<CustomerVerification, BigDecimal> {

	CustomerVerification findBycustomerIdAndVerificationType(BigDecimal customerId, String verificationType);
}
