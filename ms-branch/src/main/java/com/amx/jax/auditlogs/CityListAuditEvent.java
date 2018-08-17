package com.amx.jax.auditlogs;

import com.amx.amxlib.model.request.CommonRequest;

public class CityListAuditEvent extends JaxAuditEvent {

	CommonRequest dataModel;

	public CommonRequest getDataModel() {
		return dataModel;
	}

	public void setDataModel(CommonRequest dataModel) {
		this.dataModel = dataModel;
	}

	public CityListAuditEvent(CommonRequest dataModel) {
		super(Type.CITY_LIST);
		this.dataModel = dataModel;
		
	}

}
