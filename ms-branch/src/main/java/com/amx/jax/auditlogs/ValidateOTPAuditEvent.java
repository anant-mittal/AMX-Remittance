package com.amx.jax.auditlogs;

import com.amx.amxlib.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.auditlogs.JaxAuditEvent.Type;

public class ValidateOTPAuditEvent extends JaxAuditEvent{

	OffsiteCustomerRegistrationRequest model;
	

	public OffsiteCustomerRegistrationRequest getModel() {
		return model;
	}

	public void setModel(OffsiteCustomerRegistrationRequest model) {
		this.model = model;
	}

	public ValidateOTPAuditEvent(OffsiteCustomerRegistrationRequest model) {
		super(Type.VALIDATE_OTP);
		this.model = model;
	}}
