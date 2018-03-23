package com.amx.jax.auditlog;

import java.math.BigDecimal;

import com.amx.amxlib.constant.JaxTransactionStatus;

public class TransactionEvent extends JaxAuditEvent {

	JaxTransactionStatus transactionStatus;

	BigDecimal applicationDocumentNo;

	BigDecimal applicationDocumentFinYear;

	public BigDecimal getApplicationDocumentNo() {
		return applicationDocumentNo;
	}

	public void setApplicationDocumentNo(BigDecimal applicationDocumentNo) {
		this.applicationDocumentNo = applicationDocumentNo;
	}

	public BigDecimal getApplicationDocumentFinYear() {
		return applicationDocumentFinYear;
	}

	public void setApplicationDocumentFinYear(BigDecimal applicationDocumentFinYear) {
		this.applicationDocumentFinYear = applicationDocumentFinYear;
	}

	public JaxTransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(JaxTransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

}
