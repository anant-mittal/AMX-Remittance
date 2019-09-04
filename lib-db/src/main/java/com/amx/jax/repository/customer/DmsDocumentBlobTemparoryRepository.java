package com.amx.jax.repository.customer;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.amx.jax.dbmodel.customer.DmsDocumentBlobTemparory;

public interface DmsDocumentBlobTemparoryRepository extends CrudRepository<DmsDocumentBlobTemparory, Serializable> {

	@Procedure(procedureName = "SAVE_BLOB_DOCS_DB_JAVA")
	@Transactional
	public void copyBlobDataFromJava(@Param("P_BLOB_ID") BigDecimal blobId, @Param("P_DOCFYR") BigDecimal docFinYear);

}
