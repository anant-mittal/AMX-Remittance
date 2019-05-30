package com.amx.jax.dbmodel.customer;

import java.math.BigDecimal;
import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.amx.jax.constants.DocumentScanIndic;
import com.amx.jax.model.customer.CustomerDocUploadType;

@Entity
@Table(name = "JAX_CUST_DOC_UPLOAD_REF_TMP")
public class CustomerDocumentUploadReferenceTemp {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JAX_CUST_DOC_UPLOAD_REF_TMP_S")
	@SequenceGenerator(sequenceName = "JAX_CUST_DOC_UPLOAD_REF_TMP_S", name = "JAX_CUST_DOC_UPLOAD_REF_TMP_S")
	@Column(name = "ID")
	BigDecimal id;

	@Column(name = "SCAN_IND")
	@Enumerated(EnumType.STRING)
	DocumentScanIndic scanIndic;

	@Column(name = "DOCUMENTERA_SCAN_REF")
	String documentEraScanInd;

	@Column(name = "DB_SCAN_DOCUMENT_BLOB")
	Blob dbScanDocumentBlob;

	@Column(name = "IDENTITY_INT")
	String identityInt;

	@Column(name = "IDENTITY_TYPE_ID")
	BigDecimal identityTypeId;

	@Column(name = "UPLOAD_DOC_TYPE")
	@Enumerated(EnumType.STRING)
	CustomerDocUploadType customerDocUploadType;

	@Column(name = "UPLOAD_DATA")
	String uploadData;

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

	public String getDocumentEraScanInd() {
		return documentEraScanInd;
	}

	public void setDocumentEraScanInd(String documentEraScanInd) {
		this.documentEraScanInd = documentEraScanInd;
	}

	public Blob getDbScanDocumentBlob() {
		return dbScanDocumentBlob;
	}

	public void setDbScanDocumentBlob(Blob dbScanDocumentBlob) {
		this.dbScanDocumentBlob = dbScanDocumentBlob;
	}

	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
	}

	public BigDecimal getIdentityTypeId() {
		return identityTypeId;
	}

	public void setIdentityTypeId(BigDecimal identityTypeId) {
		this.identityTypeId = identityTypeId;
	}

	public CustomerDocUploadType getCustomerDocUploadType() {
		return customerDocUploadType;
	}

	public void setCustomerDocUploadType(CustomerDocUploadType customerDocUploadType) {
		this.customerDocUploadType = customerDocUploadType;
	}

	@Override
	public String toString() {
		return "CustomerDocumentUploadReferenceTemp [id=" + id + ", scanIndic=" + scanIndic + ", identityInt="
				+ identityInt + ", identityTypeId=" + identityTypeId + ", customerDocUploadType="
				+ customerDocUploadType + "]";
	}

	public String getUploadData() {
		return uploadData;
	}

	public void setUploadData(String uploadData) {
		this.uploadData = uploadData;
	}

}
