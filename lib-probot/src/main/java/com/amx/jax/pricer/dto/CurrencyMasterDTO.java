package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class CurrencyMasterDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private BigDecimal currencyId;
	
	private String currencyCode;
	
	private String currencyName;
	
	private String quoteName;

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getQuoteName() {
		return quoteName;
	}

	public void setQuoteName(String quoteName) {
		this.quoteName = quoteName;
	}
	
}
