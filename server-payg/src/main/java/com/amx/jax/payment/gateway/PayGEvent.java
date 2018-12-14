package com.amx.jax.payment.gateway;

import com.amx.jax.logger.AuditEvent;
import com.amx.jax.payg.PayGModel;
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

	public PayGEvent(Type type, PayGModel response) {
		super(type);
		this.response = response;
	}

	PayGParams params = null;
	PayGModel response = null;

	public PayGModel getResponse() {
		return response;
	}

	public void setResponse(PayGModel response) {
		this.response = response;
	}

	public PayGParams getParams() {
		return params;
	}

	public void setParams(PayGParams params) {
		this.params = params;
	}

}
