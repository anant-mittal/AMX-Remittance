package com.amx.jax.payment.gateway;

import com.amx.jax.logger.AuditEvent;
import com.amx.jax.payg.PayGParams;

public class PayGEvent extends AuditEvent {

	private static final long serialVersionUID = 3439794415625434212L;

	public enum Type implements EventType {
		PAYMENT_INIT, PAYMENT_CAPTURED;

		@Override
		public EventMarker marker() {
			return null;
		}
	}

	public PayGEvent(Type type, PayGParams params) {
		super(type);
		this.params = params;
	}

	public PayGEvent(Type type, PayGResponse response) {
		super(type);
		this.response = response;
	}

	PayGParams params = null;
	PayGResponse response = null;

	public PayGResponse getResponse() {
		return response;
	}

	public void setResponse(PayGResponse response) {
		this.response = response;
	}

	public PayGParams getParams() {
		return params;
	}

	public void setParams(PayGParams params) {
		this.params = params;
	}

}
