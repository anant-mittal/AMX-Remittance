package com.amx.jax.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.ServiceProviderPartner;

public interface ServiceProviderPartnerRepository extends CrudRepository<ServiceProviderPartner, Serializable> {
	@Query("select c from ServiceProviderPartner c")
	public List<ServiceProviderPartner> getServiceProviderPartner();
}
