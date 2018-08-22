package com.amx.jax.auditlogs;

import com.amx.amxlib.model.request.CommonRequest;

public class DistrictListAuditEvent extends JaxAuditEvent {

	CommonRequest dataModel;

	public CommonRequest getDataModel() {
		return dataModel;
	}

	public void setDataModel(CommonRequest dataModel) {
		this.dataModel = dataModel;
	}

	public DistrictListAuditEvent(CommonRequest dataModel) {
		super(Type.DISTRICT_LIST);
		this.dataModel = dataModel;
		
	}

}
