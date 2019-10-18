package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ReferralDetails;

public interface ReferralDetailRepository extends CrudRepository<ReferralDetails, BigDecimal> {
	@Query("select c from ReferralDetails c where customerId=?1")
	public List<ReferralDetails> getReferalByCustomerId(BigDecimal customerId);
	
	@Query("select c from ReferralDetails c where customerReferralCode=?1")
	public List<ReferralDetails> getReferalByCustomerReferralCode(String customerReferralCode);
}
