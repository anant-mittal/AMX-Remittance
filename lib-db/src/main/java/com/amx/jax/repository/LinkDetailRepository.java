package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.LinkDetails;
import com.amx.jax.dbmodel.ReferralDetails;

public interface LinkDetailRepository extends CrudRepository<LinkDetails, BigDecimal> {
	
	@Query("select l from LinkDetails l where linkId=?1")
	public List<LinkDetails> getLinkDetailsById(String linkId);
	
	@Query("select l from LinkDetails l where customerId=?1")
	public List<LinkDetails> getLinkDetailsByCustomerId(String customerId);
}
