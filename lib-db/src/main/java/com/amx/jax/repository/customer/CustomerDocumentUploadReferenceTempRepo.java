package com.amx.jax.repository.customer;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReferenceTemp;
import com.amx.jax.model.customer.CustomerDocUploadType;

public interface CustomerDocumentUploadReferenceTempRepo
		extends CrudRepository<CustomerDocumentUploadReferenceTemp, Serializable> {

	CustomerDocumentUploadReferenceTemp findByidentityIntAndIdentityTypeIdAndCustomerDocUploadType(String identityInt,
			BigDecimal identityType, CustomerDocUploadType uploadType);
}
