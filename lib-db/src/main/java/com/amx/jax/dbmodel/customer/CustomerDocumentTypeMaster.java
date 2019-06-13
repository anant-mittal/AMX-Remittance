package com.amx.jax.dbmodel.customer;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_CUSTOMER_DOC_TYPE_MASTER")
public class CustomerDocumentTypeMaster {

	@Id
	@Column(name = "ID")
	BigDecimal id;

	@Column(name = "DOC_CATEGORY")
	String documentCategory;

	@Column(name = "DOC_TYPE")
	String documentType;

	@Column(name = "ISACTIVE")
	String isActive;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getDocumentCategory() {
		return documentCategory;
	}

	public void setDocumentCategory(String documentCategory) {
		this.documentCategory = documentCategory;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
}
