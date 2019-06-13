package com.amx.jax.repository.customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReferenceTemp;
import com.amx.jax.model.customer.CustomerDocUploadCategory;

public interface CustomerDocumentUploadReferenceTempRepo
		extends CrudRepository<CustomerDocumentUploadReferenceTemp, Serializable> {

	CustomerDocumentUploadReferenceTemp findByidentityIntAndIdentityTypeIdAndCustomerDocUploadType(String identityInt,
			BigDecimal identityType, CustomerDocUploadCategory uploadType);

	List<CustomerDocumentUploadReferenceTemp> findByidentityIntAndIdentityTypeId(String identityInt,
			BigDecimal identityType);
}
