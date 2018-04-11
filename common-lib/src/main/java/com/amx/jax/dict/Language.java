package com.amx.jax.dict;

import java.math.BigDecimal;

public enum Language {
	ENG("1"), ARABIC("2");

	public static final Language DEFAULT = ENG;

	String code;

	Language(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public BigDecimal getBDCode() {
		return new BigDecimal(code);
	}

	public void setCode(String code) {
		this.code = code;
	}

}
