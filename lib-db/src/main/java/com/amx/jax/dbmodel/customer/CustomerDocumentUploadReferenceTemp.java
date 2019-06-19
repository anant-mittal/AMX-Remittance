package com.amx.jax.dbmodel.customer;

import java.math.BigDecimal;
import java.sql.Blob;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.amx.jax.constants.DocumentScanIndic;

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
	String documentEraScanRef;

	@Column(name = "DB_SCAN_DOCUMENT_BLOB")
	Blob dbScanDocumentBlob;

	@Column(name = "IDENTITY_INT")
	String identityInt;

	@Column(name = "IDENTITY_TYPE_ID")
	BigDecimal identityTypeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOC_TYPE_MASTER_ID", referencedColumnName = "ID")
	CustomerDocumentTypeMaster customerDocumentTypeMaster;

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

	public String getUploadData() {
		return uploadData;
	}

	public void setUploadData(String uploadData) {
		this.uploadData = uploadData;
	}

	public CustomerDocumentTypeMaster getCustomerDocumentTypeMaster() {
		return customerDocumentTypeMaster;
	}

	public void setCustomerDocumentTypeMaster(CustomerDocumentTypeMaster customerDocumentTypeMaster) {
		this.customerDocumentTypeMaster = customerDocumentTypeMaster;
	}

	public String getDocumentEraScanRef() {
		return documentEraScanRef;
	}

	public void setDocumentEraScanRef(String documentEraScanRef) {
		this.documentEraScanRef = documentEraScanRef;
	}

}
