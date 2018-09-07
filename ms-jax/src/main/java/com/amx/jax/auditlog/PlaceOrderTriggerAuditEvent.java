package com.amx.jax.auditlog;

import com.amx.jax.auditlog.JaxUserAuditEvent.Type;

public class PlaceOrderTriggerAuditEvent extends JaxAuditEvent {
	Object model;
	
	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}

	public PlaceOrderTriggerAuditEvent(Object model) {
		super(Type.PLACE_ORDER_TRIGGER);
		this.model = model;
	}

}
