package com.amx.jax.auditlogs;

import com.amx.jax.auditlogs.JaxAuditEvent.Type;
import com.amx.jax.model.request.OffsiteCustomerRegistrationRequest;

public class IdTypeAuditEvent extends JaxAuditEvent {

	OffsiteCustomerRegistrationRequest model;	

	public OffsiteCustomerRegistrationRequest getModel() {
		return model;
	}

	public void setModel(OffsiteCustomerRegistrationRequest model) {
		this.model = model;
	}

	public IdTypeAuditEvent(OffsiteCustomerRegistrationRequest model) {
		super(Type.ID_TYPE);
		this.model = model;
	}}
