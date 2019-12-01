package com.amx.amxlib.model.response;

public enum LanguageCodeType {
	
	English("en"), Arabic("ar");
	
	private final String languageCode;

	public String getLanguageCode() {
		return languageCode;
	}

	LanguageCodeType(String languageCode) {
		this.languageCode = languageCode;
	}

}
