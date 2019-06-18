package com.amx.jax.repository.customer;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReference;

public interface CustomerDocumentUploadReferenceRepo extends CrudRepository<CustomerDocumentUploadReference, Serializable> {

	public CustomerDocumentUploadReference findByCustomerDocumentTypeMasterAndCustomerId(CustomerDocumentTypeMaster customerDocumentTypeMaster,
			BigDecimal customerId);
}
