package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.DocumentUploadReference;

public interface IDocumentUploadMapRepository extends JpaRepository<DocumentUploadReference, Serializable> {
	@Query("select c from DocumentUploadReference c where c.customerId =?1 and c.isActive = 'Y'")
	public DocumentUploadReference getCustById(BigDecimal customerId);
}
