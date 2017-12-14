package com.amx.amxlib.meta.model;

import java.math.BigDecimal;

import com.amx.amxlib.model.AbstractModel;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyMasterDTO extends AbstractModel {

	private BigDecimal currencyId;
	private String currencyCode;
	private String currencyName;
	private String quoteName;

	@JsonProperty("decimalNumber")
	private BigDecimal decinalNumber;
	
	private BigDecimal countryId;

	@Override
	public String getModelType() {
		return "currencyMaster";
	}

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

	public BigDecimal getDecinalNumber() {
		return decinalNumber;
	}

	public void setDecinalNumber(BigDecimal decinalNumber) {
		this.decinalNumber = decinalNumber;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

}
