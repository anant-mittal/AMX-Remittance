package com.amx.jax.auditlog;

import java.math.BigDecimal;

import com.amx.jax.constants.JaxTransactionStatus;

public class JaxTransactionEvent extends JaxAuditEvent {

	private static final long serialVersionUID = 1L;

	JaxTransactionStatus transactionStatus;

	String applicationDocumentNo;

	BigDecimal applicationDocumentFinYear;

	public JaxTransactionEvent(JaxTransactionStatus transactionStatus, String applicationDocumentNo,
			BigDecimal applicationDocumentFinYear) {
		super(Type.APPLICATION_CREATED);
		this.transactionStatus = transactionStatus;
		this.applicationDocumentNo = applicationDocumentNo;
		this.applicationDocumentFinYear = applicationDocumentFinYear;
	}

	public String getApplicationDocumentNo() {
		return applicationDocumentNo;
	}

	public void setApplicationDocumentNo(String applicationDocumentNo) {
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
