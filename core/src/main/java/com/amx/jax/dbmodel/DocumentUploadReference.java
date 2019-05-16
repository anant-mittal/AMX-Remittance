package com.amx.jax.dbmodel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EX_DOCUMENT_UPLOAD_REFERENCE")
public class DocumentUploadReference {
	private BigDecimal applicationCountryId;
	private BigDecimal customerId;
	private BigDecimal uploadId;
	private String uploadType;
	private BigDecimal uploadDoctypeId;
	private BigDecimal docBlobId;
	private String uploadedFilename;
	private String isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	
	
	@Override
	public String toString() {
		return "DocumentUploadReference [applicationCountryId=" + applicationCountryId + ", customerId=" + customerId
				+ ", uploadId=" + uploadId + ", uploadType=" + uploadType + ", uploadDoctypeId=" + uploadDoctypeId
				+ ", docBlobId=" + docBlobId + ", uploadedFilename=" + uploadedFilename + ", isActive=" + isActive
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", modifiedBy=" + modifiedBy
				+ ", modifiedDate=" + modifiedDate + "]";
	}
	@Column(name="APPLICATION_COUNTRY_ID")
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
	
	@Column(name="CUSTOMER_ID")
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	
	@Id	
	@GeneratedValue(generator="ex_upload_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_upload_seq",sequenceName="EX_UPLOAD_SEQ ",allocationSize=1)
	@Column(name = "UPLOAD_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getUploadId() {
		return uploadId;
	}
	public void setUploadId(BigDecimal uploadId) {
		this.uploadId = uploadId;
	}
	
	@Column(name="UPLOAD_TYPE")
	public String getUploadType() {
		return uploadType;
	}
	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}
	
	@Column(name="UPLOAD_DOCTYPE_ID")
	public BigDecimal getUploadDoctypeId() {
		return uploadDoctypeId;
	}
	public void setUploadDoctypeId(BigDecimal uploadDoctypeId) {
		this.uploadDoctypeId = uploadDoctypeId;
	}
	
	@Column(name="DOC_BLOB_ID")
	public BigDecimal getDocBlobId() {
		return docBlobId;
	}
	public void setDocBlobId(BigDecimal docBlobId) {
		this.docBlobId = docBlobId;
	}
	
	@Column(name="UPLOADED_FILENAME")
	public String getUploadedFilename() {
		return uploadedFilename;
	}
	public void setUploadedFilename(String uploadedFilename) {
		this.uploadedFilename = uploadedFilename;
	}
	
	@Column(name="ISACTIVE")
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	@Column(name="CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name="CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name="MODIFIED_BY")
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	@Column(name="MODIFIED_DATE")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
}
