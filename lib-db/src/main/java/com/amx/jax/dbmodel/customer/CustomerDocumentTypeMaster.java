package com.amx.jax.dbmodel.customer;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.amx.jax.dbmodel.JaxField;

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

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "JAX_CUST_DOC_FIELD_MAPPING", joinColumns = @JoinColumn(name = "CUSTOMER_DOC_TYPE_MASTER_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "JAX_FIELD_NAME", referencedColumnName = "NAME"))
	Set<JaxField> jaxFields;

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

	public Set<JaxField> getJaxFields() {
		return jaxFields;
	}

	public void setJaxFields(Set<JaxField> jaxFields) {
		this.jaxFields = jaxFields;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerDocumentTypeMaster other = (CustomerDocumentTypeMaster) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
