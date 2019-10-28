package com.amx.jax.dbmodel.customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_CUST_DOC_TYPE_DESC")
public class CustomerDocumentTypeDesc {

	@Id
	@Column(name = "DOC_TYPE")
	String documentType;

	@Column(name = "DESCRIPTION")
	String description;

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
