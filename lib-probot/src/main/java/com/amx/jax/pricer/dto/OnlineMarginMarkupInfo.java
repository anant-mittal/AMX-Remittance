package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class OnlineMarginMarkupInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal onlineMarginMarkupId;

	private BigDecimal marginMarkup;
	
	private BigDecimal bankId;
	
	private BigDecimal countryId;
	
	private BigDecimal currencyId;
	
	private String empName;

	public BigDecimal getOnlineMarginMarkupId() {
		return onlineMarginMarkupId;
	}

	public void setOnlineMarginMarkupId(BigDecimal onlineMarginMarkupId) {
		this.onlineMarginMarkupId = onlineMarginMarkupId;
	}

	public BigDecimal getMarginMarkup() {
		return marginMarkup;
	}

	public void setMarginMarkup(BigDecimal marginMarkup) {
		this.marginMarkup = marginMarkup;
	}

	public BigDecimal getBankId() {
		return bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}


	
	
}
