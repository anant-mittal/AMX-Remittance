package com.amx.amxlib.model.response;

import com.amx.amxlib.model.AbstractModel;

public class BooleanResponse extends AbstractModel {

	boolean success;

	public BooleanResponse(boolean success) {
		super();
		this.success = success;
	}

	@Override
	public String getModelType() {
		return "boolean_response";
	}
}
