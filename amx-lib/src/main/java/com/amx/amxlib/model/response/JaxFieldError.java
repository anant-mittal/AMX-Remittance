package com.amx.amxlib.model.response;

public class JaxFieldError {

	String fieldName;
	
	

	public JaxFieldError() {
		super();
		// TODO Auto-generated constructor stub
	}

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
