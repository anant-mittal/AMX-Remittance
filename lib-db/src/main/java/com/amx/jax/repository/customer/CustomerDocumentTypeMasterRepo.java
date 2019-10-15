package com.amx.jax.repository.customer;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;

public interface CustomerDocumentTypeMasterRepo extends CrudRepository<CustomerDocumentTypeMaster, Serializable> {

	CustomerDocumentTypeMaster findByDocumentCategoryAndDocumentType(String docCategory, String docType);

	@Query(value = "SELECT DISTINCT(DOC_CATEGORY) FROM JAX_CUSTOMER_DOC_TYPE_MASTER WHERE RENDER='Y'", nativeQuery = true)
	public List<String> getDistinctDocumentCategory();

	public List<CustomerDocumentTypeMaster> findByDocumentCategory(String documentCategory);
}
