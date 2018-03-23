package com.amx.jax.postman.model;

public enum Langs {

	AR_KW("ar_KW"), EN_US("en_US");

	public static Langs DEFAULT = AR_KW;

	public String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	Langs(String code) {
		this.code = code;
	}

}
