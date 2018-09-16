package com.amx.jax.auditlogs;

import com.amx.jax.model.request.CommonRequest;

public class StateListAuditEvent extends JaxAuditEvent {

	CommonRequest dataModel;

	public CommonRequest getDataModel() {
		return dataModel;
	}

	public void setDataModel(CommonRequest dataModel) {
		this.dataModel = dataModel;
	}

	public StateListAuditEvent(CommonRequest dataModel) {
		super(Type.STATE_LIST);
		this.dataModel = dataModel;
		
	}

}
