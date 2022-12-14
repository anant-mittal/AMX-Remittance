package com.amx.jax.dbmodel.customer;

import java.math.BigDecimal;
import java.util.Date;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.amx.jax.constants.DocumentScanIndic;

@Entity
@Table(name = "JAX_CUST_DOC_UPLOAD_REF")
public class CustomerDocumentUploadReference {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JAX_CUST_DOC_UPLOAD_REF_SEQ")
	@SequenceGenerator(sequenceName = "JAX_CUST_DOC_UPLOAD_REF_SEQ", name = "JAX_CUST_DOC_UPLOAD_REF_SEQ", allocationSize = 1)
	@Column(name = "ID")
	BigDecimal id;

	@Column(name = "SCAN_IND")
	@Enumerated(EnumType.STRING)
	DocumentScanIndic scanIndic;

	@Column(name = "CUSTOMER_ID")
	BigDecimal customerId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOC_TYPE_MASTER_ID", referencedColumnName = "ID")
	CustomerDocumentTypeMaster customerDocumentTypeMaster;

	@Column(name = "STATUS")
	String status;

	@OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "ID", referencedColumnName = "CUST_DOC_UPLOAD_REF_ID")
	DbScanRef dbScanRef;
	
	@Column(name = "CREATED_AT")
	Date createdAt;
	
	@Column(name="EXPIRY_DATE")
	Date expiryDate;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public DocumentScanIndic getScanIndic() {
		return scanIndic;
	}

	public void setScanIndic(DocumentScanIndic scanIndic) {
		this.scanIndic = scanIndic;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public CustomerDocumentTypeMaster getCustomerDocumentTypeMaster() {
		return customerDocumentTypeMaster;
	}

	public void setCustomerDocumentTypeMaster(CustomerDocumentTypeMaster customerDocumentTypeMaster) {
		this.customerDocumentTypeMaster = customerDocumentTypeMaster;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public DbScanRef getDbScanRef() {
		return dbScanRef;
	}

	public void setDbScanRef(DbScanRef dbScanRef) {
		this.dbScanRef = dbScanRef;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
}
