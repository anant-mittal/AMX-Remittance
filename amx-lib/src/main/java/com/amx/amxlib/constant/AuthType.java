package com.amx.amxlib.constant;

import java.math.BigDecimal;

public enum AuthType {

	NEW_BENE_TRANSACT_AMOUNT_LIMIT("29"), NEW_BENE_TRANSACT_TIME_LIMIT("31");

	BigDecimal authType;

	private AuthType(String authType) {
		this.authType = new BigDecimal(authType);
	}

	public BigDecimal getAuthType() {
		return authType;
	}

	public void setAuthType(BigDecimal authType) {
		this.authType = authType;
	}
}
