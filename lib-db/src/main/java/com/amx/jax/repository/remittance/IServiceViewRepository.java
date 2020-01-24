package com.amx.jax.repository.remittance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.ViewServiceDetails;

public interface IServiceViewRepository extends CrudRepository<ViewServiceDetails, Serializable>{
	public List<ViewServiceDetails> findByServiceMasterId(BigDecimal serviceMasterId);
	@Query("select c from ViewServiceDetails c")
	public List<ViewServiceDetails> getServiceMaster();
	

}
