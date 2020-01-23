package com.amx.jax.complaince;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReference;

public interface ComplainceRepo extends CrudRepository<CustomerDocumentUploadReference, Serializable> {

	List<CustomerDocumentUploadReference> findByCustomerId(BigDecimal customerId);
	
}
