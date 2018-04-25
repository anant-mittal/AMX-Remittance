package com.amx.amxlib.constant;

public enum AuthType {

	NEW_BENE_TRANSACT_AMOUNT_LIMIT("29"), NEW_BENE_TRANSACT_TIME_LIMIT("31"), MAX_DOM_AMOUNT_LIMIT("100");

	String authType;

	private AuthType(String authType) {
		this.authType = authType;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

}
