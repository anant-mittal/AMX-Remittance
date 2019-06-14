package com.amx.jax.repository.customer;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;

public interface CustomerDocumentTypeMasterRepo extends CrudRepository<CustomerDocumentTypeMaster, Serializable> {

	CustomerDocumentTypeMaster findByDocumentCategoryAndDocumentType(String docCategory, String docType);
}
