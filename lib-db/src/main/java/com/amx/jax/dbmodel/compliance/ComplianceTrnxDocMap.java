package com.amx.jax.dbmodel.compliance;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.amx.jax.client.compliance.ComplianceTrnxStatus;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReference;

@Entity
@Table(name = "JAX_COMP_CUST_DOC_TRNX_MAP")
public class ComplianceTrnxDocMap {

	@Id
	@Column(name = "ID")
	BigDecimal id;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	ComplianceTrnxStatus status;

	@Column(name = "REMITTANCE_TRANSACTION_ID")
	BigDecimal remittanceTransaction;

	@JoinColumn(name = "CUST_DOC_UPLOAD_REF_ID")
	@OneToOne
	CustomerDocumentUploadReference customerDocumentUploadReference;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public ComplianceTrnxStatus getStatus() {
		return status;
	}

	public void setStatus(ComplianceTrnxStatus status) {
		this.status = status;
	}

	public BigDecimal getRemittanceTransaction() {
		return remittanceTransaction;
	}

	public void setRemittanceTransaction(BigDecimal remittanceTransaction) {
		this.remittanceTransaction = remittanceTransaction;
	}

	public CustomerDocumentUploadReference getCustomerDocumentUploadReference() {
		return customerDocumentUploadReference;
	}

	public void setCustomerDocumentUploadReference(CustomerDocumentUploadReference customerDocumentUploadReference) {
		this.customerDocumentUploadReference = customerDocumentUploadReference;
	}

}
