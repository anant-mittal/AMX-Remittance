package com.amx.jax.repository.customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReferenceTemp;

public interface CustomerDocumentUploadReferenceTempRepo extends CrudRepository<CustomerDocumentUploadReferenceTemp, Serializable> {

	CustomerDocumentUploadReferenceTemp findByidentityIntAndIdentityTypeIdAndCustomerDocumentTypeMaster(String identityInt, BigDecimal identityType,
			CustomerDocumentTypeMaster customerDocumentTypeMaster);

	List<CustomerDocumentUploadReferenceTemp> findByidentityIntAndIdentityTypeId(String identityInt, BigDecimal identityType);
}
