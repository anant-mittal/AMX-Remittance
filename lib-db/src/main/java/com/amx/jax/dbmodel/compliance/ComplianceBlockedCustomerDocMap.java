package com.amx.jax.dbmodel.compliance;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.amx.jax.client.compliance.ComplianceTrnxdDocStatus;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReference;

@Entity
@Table(name = "JAX_COMP_BLOCKED_CUST_DOC_MAP")
public class ComplianceBlockedCustomerDocMap {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "JAX_COMP_BLOCKED_CUST_DOC_MAP_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(sequenceName = "JAX_COMP_BLOCKED_CUST_DOC_MAP_SEQ", name = "JAX_COMP_BLOCKED_CUST_DOC_MAP_SEQ", allocationSize = 1)
	BigDecimal id;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	ComplianceTrnxdDocStatus status;

	@JoinColumn(name = "CUST_DOC_UPLOAD_REF_ID")
	@OneToOne
	CustomerDocumentUploadReference customerDocumentUploadReference;

	@Column(name = "CUSTOMER_ID")
	BigDecimal customerId;

	@JoinColumn(name = "DOC_TYPE_MASTER_ID")
	@OneToOne
	CustomerDocumentTypeMaster docTypeMaster;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public ComplianceTrnxdDocStatus getStatus() {
		return status;
	}

	public void setStatus(ComplianceTrnxdDocStatus status) {
		this.status = status;
	}

	public CustomerDocumentUploadReference getCustomerDocumentUploadReference() {
		return customerDocumentUploadReference;
	}

	public void setCustomerDocumentUploadReference(CustomerDocumentUploadReference customerDocumentUploadReference) {
		this.customerDocumentUploadReference = customerDocumentUploadReference;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public CustomerDocumentTypeMaster getDocTypeMaster() {
		return docTypeMaster;
	}

	public void setDocTypeMaster(CustomerDocumentTypeMaster docTypeMaster) {
		this.docTypeMaster = docTypeMaster;
	}
}
