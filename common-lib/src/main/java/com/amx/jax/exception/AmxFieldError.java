package com.amx.jax.exception;

public class AmxFieldError {

	String fieldName;
	
	

	public AmxFieldError() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AmxFieldError(String fieldName) {
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
