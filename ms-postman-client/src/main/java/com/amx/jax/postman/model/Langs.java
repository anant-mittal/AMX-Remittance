package com.amx.jax.postman.model;

public enum Langs {

	KW("ar_KW"), ENG("en_US");

	String langCode = null;

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	Langs(String langCode) {
		this.langCode = langCode;
	}
}
