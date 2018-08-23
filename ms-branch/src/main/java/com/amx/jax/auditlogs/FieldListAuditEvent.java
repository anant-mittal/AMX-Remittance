package com.amx.jax.auditlogs;

import com.amx.jax.model.request.DynamicFieldRequest;

public class FieldListAuditEvent extends JaxAuditEvent {

	DynamicFieldRequest dynamicFieldModel;

	public DynamicFieldRequest getDynamicFieldModel() {
		return dynamicFieldModel;
	}

	public void setDynamicFieldModel(DynamicFieldRequest dynamicFieldModel) {
		this.dynamicFieldModel = dynamicFieldModel;
	}

	public FieldListAuditEvent(DynamicFieldRequest dynamicFieldModel) {
		super(Type.FIELD_LIST);
		this.dynamicFieldModel = dynamicFieldModel;
	}

}
