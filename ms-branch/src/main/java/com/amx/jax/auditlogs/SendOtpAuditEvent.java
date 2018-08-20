package com.amx.jax.auditlogs;

import com.amx.amxlib.model.request.OffsiteCustomerRegistrationRequest;

public class SendOtpAuditEvent extends JaxAuditEvent {

	OffsiteCustomerRegistrationRequest model;
	

	public OffsiteCustomerRegistrationRequest getModel() {
		return model;
	}

	public void setModel(OffsiteCustomerRegistrationRequest model) {
		this.model = model;
	}

	public SendOtpAuditEvent(OffsiteCustomerRegistrationRequest model) {
		super(Type.SEND_OTP);
		this.model = model;
	}}
