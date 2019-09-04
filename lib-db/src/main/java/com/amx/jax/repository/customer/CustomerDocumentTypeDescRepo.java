package com.amx.jax.repository.customer;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.customer.CustomerDocumentTypeDesc;

public interface CustomerDocumentTypeDescRepo extends CrudRepository<CustomerDocumentTypeDesc, Serializable> {

	public CustomerDocumentTypeDesc findByDocumentType(String docType);

}
