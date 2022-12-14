package com.amx.jax.repository.customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReference;

public interface CustomerDocumentUploadReferenceRepo extends CrudRepository<CustomerDocumentUploadReference, Serializable> {

	public List<CustomerDocumentUploadReference> findByCustomerDocumentTypeMasterAndCustomerId(CustomerDocumentTypeMaster customerDocumentTypeMaster,
			BigDecimal customerId);

	public List<CustomerDocumentUploadReference> findByCustomerId(BigDecimal customerId);

	public List<CustomerDocumentUploadReference> findByCustomerIdAndStatusIn(BigDecimal customerId, List<String> status);

	public List<CustomerDocumentUploadReference> findByCustomerDocumentTypeMasterAndCustomerIdAndStatus(
			CustomerDocumentTypeMaster customerDocumentTypeMaster, BigDecimal customerId, String status);
}
