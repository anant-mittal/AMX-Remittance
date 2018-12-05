package com.amx.jax.model.response.fx;

import java.io.Serializable;

public class CurrencyDenominationTypeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2990440073403003762L;
	private String currencyDenominationCode;
	private String currencyDenominationDesc;
	public String getCurrencyDenominationCode() {
		return currencyDenominationCode;
	}
	public void setCurrencyDenominationCode(String currencyDenominationCode) {
		this.currencyDenominationCode = currencyDenominationCode;
	}
	public String getCurrencyDenominationDesc() {
		return currencyDenominationDesc;
	}
	public void setCurrencyDenominationDesc(String currencyDenominationDesc) {
		this.currencyDenominationDesc = currencyDenominationDesc;
	}
	
}
