package com.amx.jax.repository;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.ContactLinkDetails;


public interface ContactLinkDetailsRepository extends CrudRepository<ContactLinkDetails, BigDecimal> {

}

