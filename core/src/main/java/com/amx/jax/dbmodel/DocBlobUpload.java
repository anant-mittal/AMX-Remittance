package com.amx.jax.dbmodel;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DMS_DOC_BLOB_UPLOAD_JAVA")
public class DocBlobUpload implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -838181138590494662L;
	private BigDecimal docBlobID;
	private BigDecimal cntryCd;
	private BigDecimal seqNo;
	private BigDecimal docFinYear;
	private byte[] docContent;
	private String createdBy;
	private Date createdOn;
	private Date updatedDate;
	private String updateBy;
	
	@Column(name = "DOC_BLOB_ID")
	public BigDecimal getDocBlobID() {
		return docBlobID;
	}

	public void setDocBlobID(BigDecimal docBlobID) {
		this.docBlobID = docBlobID;
	}

	
	@Column(name = "CNTRYCD")
	public BigDecimal getCntryCd() {
		return cntryCd;
	}

	public void setCntryCd(BigDecimal cntryCd) {
		this.cntryCd = cntryCd;
	}
	@Id
	@Column(name = "SEQ_NO")
	public BigDecimal getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(BigDecimal seqNo) {
		this.seqNo = seqNo;
	}

	@Column(name = "DOC_FIN_YR")
	public BigDecimal getDocFinYear() {
		return docFinYear;
	}

	public void setDocFinYear(BigDecimal docFinYear) {
		this.docFinYear = docFinYear;
	}

	@Column(name = "DOC_CONTENT")
	public byte[] getDocContent() {
		return docContent;
	}

	public void setDocContent(byte[] fileContent) {
		this.docContent = fileContent;
	}

	@Column(name = "CRE_BY")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "CRE_DT")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "UPD_DT")
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(name = "UPD_BY")
	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

}
