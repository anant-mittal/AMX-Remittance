package com.amx.amxlib.model.response;

import com.amx.amxlib.model.AbstractModel;

public class BooleanResponse extends AbstractModel {

	private boolean success;
	
	

	public BooleanResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BooleanResponse(boolean success) {
		super();
		this.success = success;
	}

	@Override
	public String getModelType() {
		return "boolean_response";
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
