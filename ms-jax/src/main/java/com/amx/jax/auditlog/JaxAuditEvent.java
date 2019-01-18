package com.amx.jax.auditlog;

import java.math.BigDecimal;

import com.amx.jax.logger.AuditEvent;

public class JaxAuditEvent extends AuditEvent {

	private static final long serialVersionUID = 7451732272992078549L;
	BigDecimal customerId;

	Boolean success;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public static enum Type implements EventType {
		APPLICATION_CREATED,
		PLACE_ORDER_TRIGGER,
		;
		/** End Here **/

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
