package com.amx.jax.dbmodel;

/**
 * @author rabil
 * @date :18/11/2018
 * @purpose: to store pag details
 * 
 */
import java.io.Serializable;
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
@Table(name = "EX_PAYG_TRANSACTION_DETAILS")
public class PaygDetailsModel implements Serializable{
	

	private static final long serialVersionUID = 8141786552536491645L;

	@Id
	@GeneratedValue(generator="EX_PAYG_TRANSACTION_SEQ",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="EX_PAYG_TRANSACTION_SEQ",sequenceName="EX_PAYG_TRANSACTION_SEQ",allocationSize=1)
	@Column(name="PAYG_TRNX_DTLS_ID", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal paygTrnxSeqId;
	
	@Column(name="COLLECTION_DOCUMENT_NO")
	private BigDecimal collDocNumber;
	
	@Column(name="COLLECTION_DOC_FINANCE_YEAR")
	private BigDecimal collDocFYear;
	
	@Column(name="CUSTOMER_ID")
	private BigDecimal customerId;
	
	
	@Column(name="PG_PAYMENT_ID")
	private String pgPaymentId;
	
	@Column(name="PG_REFERENCE_ID")
	private String pgReferenceId;
	
	@Column(name="PG_TRANSACTION_ID")
	private String pgTransactionId;
	
	@Column(name="PG_AUTH_CODE")
	private String pgAuthCode;
	
	@Column(name="PG_ERROR_TEXT")
	private String pgErrorText;
	
	@Column(name="PG_RCEIPT_DATE")
	private String pgReceiptDate;
	
	@Column(name="ERROR_MESSAGE")
	private String errorMessage;
	
	@Column(name="TRANSACTION_TYPE")
	private String trnxType;
	
	@Column(name="PG_RESULT_CODE")
	private String resultCode;
	
	@Column(name="CREATION_DATE")
	private Date creationDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	

	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	 
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="REMARKS")
	private String remarks;

	public BigDecimal getPaygTrnxSeqId() {
		return paygTrnxSeqId;
	}

	public void setPaygTrnxSeqId(BigDecimal paygTrnxSeqId) {
		this.paygTrnxSeqId = paygTrnxSeqId;
	}

	public BigDecimal getCollDocNumber() {
		return collDocNumber;
	}

	public void setCollDocNumber(BigDecimal collDocNumber) {
		this.collDocNumber = collDocNumber;
	}

	public BigDecimal getCollDocFYear() {
		return collDocFYear;
	}

	public void setCollDocFYear(BigDecimal collDocFYear) {
		this.collDocFYear = collDocFYear;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public String getPgPaymentId() {
		return pgPaymentId;
	}

	public void setPgPaymentId(String pgPaymentId) {
		this.pgPaymentId = pgPaymentId;
	}

	public String getPgReferenceId() {
		return pgReferenceId;
	}

	public void setPgReferenceId(String pgReferenceId) {
		this.pgReferenceId = pgReferenceId;
	}

	public String getPgTransactionId() {
		return pgTransactionId;
	}

	public void setPgTransactionId(String pgTransactionId) {
		this.pgTransactionId = pgTransactionId;
	}

	public String getPgAuthCode() {
		return pgAuthCode;
	}

	public void setPgAuthCode(String pgAuthCode) {
		this.pgAuthCode = pgAuthCode;
	}

	public String getPgErrorText() {
		return pgErrorText;
	}

	public void setPgErrorText(String pgErrorText) {
		this.pgErrorText = pgErrorText;
	}

	public String getPgReceiptDate() {
		return pgReceiptDate;
	}

	public void setPgReceiptDate(String pgReceiptDate) {
		this.pgReceiptDate = pgReceiptDate;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getTrnxType() {
		return trnxType;
	}

	public void setTrnxType(String trnxType) {
		this.trnxType = trnxType;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
