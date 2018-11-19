package com.amx.jax.dbmodel;

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
	//private BigDecimal temSeqNo;
	private BigDecimal docBlobID;
	private BigDecimal cntryCd;
	private BigDecimal seqNo;
	private BigDecimal docFinYear;
	//private byte[] docContent;
	private Blob docContent;
	private String createdBy;
	private Date createdOn;
	private Date updatedDate;
	private String updateBy;

	/*@Id
	@GeneratedValue(generator = "ex_dms_doc_blob_temp_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_dms_doc_blob_temp_seq", sequenceName = "EX_DMS_DOC_BLOB_TEMP_SEQ", allocationSize = 1)
	@Column(name = "TEMP_SEQNO", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getTemSeqNo() {
		return temSeqNo;
	}

	public void setTemSeqNo(BigDecimal temSeqNo) {
		this.temSeqNo = temSeqNo;
	}*/

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

	/*@Column(name = "DOC_CONTENT")
	public byte[] getDocContent() {
		return docContent;
	}

	public void setDocContent(byte[] fileContent) {
		this.docContent = fileContent;
	}*/

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

	@Column(name = "DOC_CONTENT")
	public Blob getDocContent() {
		return docContent;
	}

	public void setDocContent(Blob docContent) {
		this.docContent = docContent;
	}
	

}
