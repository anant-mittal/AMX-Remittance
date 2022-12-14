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
@Table(name = "EX_DMS_APPL_MAP")
public class DmsApplMapping {
	private BigDecimal docSeqNo;
	private BigDecimal applicationCountryId;
	private BigDecimal financialYear;
	private BigDecimal docBlobId;
	private BigDecimal customerId;
	private String identityInt;
	private BigDecimal identityIntId;
	private Date identityExpiryDate;
	private String docFormat;
	private Date createdOn;
	private String createdBy;
	private String uploadType;
	
	@Column(name = "UPLOAD_TYPE")
	public String getUploadType() {
		return uploadType;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

	@Id	
	@GeneratedValue(generator="ex_dms_appl_map_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_dms_appl_map_seq",sequenceName="EX_DMS_APPL_MAP_SEQ",allocationSize=1)
	@Column(name = "DOC_SEQNO", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getDocSeqNo() {
		return docSeqNo;
	}

	public void setDocSeqNo(BigDecimal docSeqNo) {
		this.docSeqNo = docSeqNo;
	}

	@Column(name = "APPLICATION_COUNTRY_ID")
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	@Column(name = "DOC_FIN_YR")
	public BigDecimal getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(BigDecimal financialYear) {
		this.financialYear = financialYear;
	}

	@Column(name = "DOC_BLOB_ID")
	public BigDecimal getDocBlobId() {
		return docBlobId;
	}

	public void setDocBlobId(BigDecimal docBlobId) {
		this.docBlobId = docBlobId;
	}

	@Column(name = "CUSTOMER_ID")
	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	@Column(name = "IDENTITY_INT")
	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
	}

	@Column(name = "IDENTITY_TYPE_ID")
	public BigDecimal getIdentityIntId() {
		return identityIntId;
	}

	public void setIdentityIntId(BigDecimal identityIntId) {
		this.identityIntId = identityIntId;
	}

	@Column(name = "IDENTITY_EXPIRY_DATE")
	public Date getIdentityExpiryDate() {
		return identityExpiryDate;
	}

	public void setIdentityExpiryDate(Date identityExpiryDate) {
		this.identityExpiryDate = identityExpiryDate;
	}

	@Column(name = "DOC_FORMAT")
	public String getDocFormat() {
		return docFormat;
	}

	public void setDocFormat(String docFormat) {
		this.docFormat = docFormat;
	}

	@Column(name = "CREATION_DATE")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}
