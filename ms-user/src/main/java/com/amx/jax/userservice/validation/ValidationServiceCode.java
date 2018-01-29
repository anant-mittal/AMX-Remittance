package com.amx.jax.userservice.validation;

public enum ValidationServiceCode {
	KWT("91"), BAH("104"), OMN("82");

	public static final ValidationServiceCode DEFAULT = KWT;

	private String code;

	ValidationServiceCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}