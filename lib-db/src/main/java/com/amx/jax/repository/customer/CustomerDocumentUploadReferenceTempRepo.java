package com.amx.jax.repository.customer;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReferenceTemp;

public interface CustomerDocumentUploadReferenceTempRepo
		extends CrudRepository<CustomerDocumentUploadReferenceTemp, Serializable> {

}
