package com.amx.jax.auditlogs;

import com.amx.amxlib.model.request.GetJaxFieldRequest;

public class FieldListAuditEvent extends JaxAuditEvent {

	GetJaxFieldRequest detJaxFieldModel;	

	public GetJaxFieldRequest getDetJaxFieldModel() {
		return detJaxFieldModel;
	}

	public void setDetJaxFieldModel(GetJaxFieldRequest detJaxFieldModel) {
		this.detJaxFieldModel = detJaxFieldModel;
	}

	public FieldListAuditEvent(GetJaxFieldRequest detJaxFieldModel) {
		super(Type.FIELD_LIST);
		this.detJaxFieldModel = detJaxFieldModel;
	}

}
