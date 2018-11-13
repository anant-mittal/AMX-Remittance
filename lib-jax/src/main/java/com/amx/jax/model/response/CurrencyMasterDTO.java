package com.amx.jax.model.response;

import java.math.BigDecimal;

import com.amx.jax.model.AbstractModel;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyMasterDTO extends AbstractModel {

	private static final long serialVersionUID = -8611903515046787912L;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((countryId == null) ? 0 : countryId.hashCode());
		result = prime * result + ((currencyId == null) ? 0 : currencyId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurrencyMasterDTO other = (CurrencyMasterDTO) obj;
		if (countryId == null) {
			if (other.countryId != null)
				return false;
		} else if (!countryId.equals(other.countryId))
			return false;
		if (currencyId == null) {
			if (other.currencyId != null)
				return false;
		} else if (!currencyId.equals(other.currencyId))
			return false;
		return true;
	}

}
