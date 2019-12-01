package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class OnlineMarginMarkupReq implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "BankId can not be Null or Empty")
	private BigDecimal bankId;
	
	@NotNull(message = "CountryId can not be Null or Empty")
	private BigDecimal countryId;
	
	@NotNull(message = "CurrencyId can not be Null or Empty")
	private BigDecimal currencyId;
	
	private BigDecimal applicationCountryId;
	
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
	
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	@Override
	public String toString() {
		return "OnlineMarginMarkupReq [bankId=" + bankId + ", countryId=" + countryId + ", currencyId=" + currencyId
				+ "]";
	}


	
	
}
