package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CustomerTeleMarketingDetails;

public interface CustomerTeleMarketingDetailsRepository
		extends CrudRepository<CustomerTeleMarketingDetails, BigDecimal> {
	@Query("select c from CustomerTeleMarketingDetails c where leadId=?1")
	public List<CustomerTeleMarketingDetails> getCustomerTeleMarketingDetailsByLeadId(BigDecimal leadId);

	@Query("select c from CustomerTeleMarketingDetails c where customerId=?1")
	public List<CustomerTeleMarketingDetails> getCustomerTeleMarketingDetailsByCustomerId(BigDecimal customerId);
}
