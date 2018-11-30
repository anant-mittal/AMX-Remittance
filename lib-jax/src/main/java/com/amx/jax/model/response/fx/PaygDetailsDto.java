package com.amx.jax.model.response.fx;

import java.math.BigDecimal;




public class PaygDetailsDto {

	
	private BigDecimal paygTrnxSeqId;
	private BigDecimal customerId;
	private String pgPaymentId;
	private String pgReferenceId;
	private String pgTransactionId;
	private String pgAuthCode;
	private String pgErrorText;
	private String pgReceiptDate;
	private String errorMessage;
	private String trnxType;
	


	private String resultCode;


	public BigDecimal getPaygTrnxSeqId() {
		return paygTrnxSeqId;
	}


	public void setPaygTrnxSeqId(BigDecimal paygTrnxSeqId) {
		this.paygTrnxSeqId = paygTrnxSeqId;
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

	
}
