package com.amx.jax.dbmodel.customer;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_DB_SCAN_REF")
public class DbScanRef {

	@Id
	@Column(name = "CUST_DOC_UPLOAD_REF_ID")
	BigDecimal customerDocUploadRefId;

	@Column(name = "BLOB_ID")
	BigDecimal blobId;
	
	@Column(name="DOC_FORMAT")
	String docFormat;

	public BigDecimal getCustomerDocUploadRefId() {
		return customerDocUploadRefId;
	}

	public void setCustomerDocUploadRefId(BigDecimal customerDocUploadRefId) {
		this.customerDocUploadRefId = customerDocUploadRefId;
	}

	public BigDecimal getBlobId() {
		return blobId;
	}

	public void setBlobId(BigDecimal blobId) {
		this.blobId = blobId;
	}

	public String getDocFormat() {
		return docFormat;
	}

	public void setDocFormat(String docFormat) {
		this.docFormat = docFormat;
	}

}
