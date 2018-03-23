package com.amx.jax.auditlog;

import java.math.BigDecimal;

import com.amx.jax.logger.AuditEvent;

public class JaxAuditEvent extends AuditEvent {

	BigDecimal customerId;

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
}
