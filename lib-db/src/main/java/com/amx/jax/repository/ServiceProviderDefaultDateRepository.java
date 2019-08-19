package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.ServiceProviderRevenueModel;

public interface ServiceProviderDefaultDateRepository extends CrudRepository<ServiceProviderRevenueModel, Serializable>{
	@Query("select max(c.uploadDate) from ServiceProviderRevenueModel c where c.applicationCountryId=?1 and c.tpcCode=?2")
	public Date getServiceProviderRevenueModel(BigDecimal applicationCountryId,String tpcCode);
}
