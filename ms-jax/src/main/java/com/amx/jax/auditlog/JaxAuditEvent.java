package com.amx.jax.auditlog;

import java.math.BigDecimal;

import com.amx.jax.logger.AuditEvent;
import com.amx.jax.logger.AbstractEvent.EventMarker;
import com.amx.jax.logger.AbstractEvent.EventType;

public class JaxAuditEvent extends AuditEvent {

	private static final long serialVersionUID = 7451732272992078549L;
	BigDecimal customerId;
	
	public static enum Type implements EventType {
		APPLICATION_CREATED;

		@Override
		public EventMarker marker() {
			return EventMarker.AUDIT;
		}
	}


	public JaxAuditEvent(EventType type) {
		super(type);
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
}
