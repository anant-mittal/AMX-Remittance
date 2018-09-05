package com.amx.jax.auditlogs;

import com.amx.jax.model.request.OffsiteCustomerRegistrationRequest;

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
