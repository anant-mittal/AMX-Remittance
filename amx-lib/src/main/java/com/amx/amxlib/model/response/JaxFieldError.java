package com.amx.amxlib.model.response;

public class JaxFieldError {

	String fieldName;

	public JaxFieldError(String fieldName) {
		super();
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
