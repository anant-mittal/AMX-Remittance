package com.amx.jax.auditlogs;

import com.amx.amxlib.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.auditlogs.JaxAuditEvent.Type;

public class CountryListAuditEvent extends JaxAuditEvent {

	OffsiteCustomerRegistrationRequest model;
	

	public OffsiteCustomerRegistrationRequest getModel() {
		return model;
	}

	public void setModel(OffsiteCustomerRegistrationRequest model) {
		this.model = model;
	}

	public CountryListAuditEvent(OffsiteCustomerRegistrationRequest model) {
		super(Type.COUNTRY_LIST);
		this.model = model;
	}
}
