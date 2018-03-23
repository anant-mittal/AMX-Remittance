package com.amx.jax.auditlog;

import com.amx.amxlib.constant.JaxTransactionStatus;

public class TransactionEvent extends JaxAuditEvent {

	JaxTransactionStatus transactionStatus;

	public JaxTransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(JaxTransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

}
