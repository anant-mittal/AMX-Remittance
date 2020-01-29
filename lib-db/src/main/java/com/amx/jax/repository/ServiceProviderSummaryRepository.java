package com.amx.jax.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.ServiceProviderSummaryModel;

public interface ServiceProviderSummaryRepository extends CrudRepository<ServiceProviderSummaryModel, Serializable> {
	@Query("select c from ServiceProviderSummaryModel c")
	public List<ServiceProviderSummaryModel> getServiceProviderSummary();
}
