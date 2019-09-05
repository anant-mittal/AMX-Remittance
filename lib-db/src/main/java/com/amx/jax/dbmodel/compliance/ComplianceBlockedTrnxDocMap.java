package com.amx.jax.dbmodel.compliance;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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
@Table(name = "JAX_COMP_CUST_DOC_TRNX_MAP")
public class ComplianceBlockedTrnxDocMap {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "JAX_COMP_CUST_DOC_TRNX_MAP_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(sequenceName = "JAX_COMP_CUST_DOC_TRNX_MAP_SEQ", name = "JAX_COMP_CUST_DOC_TRNX_MAP_SEQ", allocationSize = 1)
	BigDecimal id;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	ComplianceTrnxdDocStatus status;

	@Column(name = "REMITTANCE_TRANSACTION_ID")
	BigDecimal remittanceTransaction;

	@JoinColumn(name = "CUST_DOC_UPLOAD_REF_ID")
	@OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
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
