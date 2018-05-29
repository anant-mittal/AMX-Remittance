package com.amx.amxlib.meta.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ViewStateDto implements Serializable {

	private static final long serialVersionUID = 8603598538756737796L;

	private BigDecimal stateId;

	private BigDecimal countryId;
	private BigDecimal languageId;
	private String stateCode;
	private String stateName;

	public BigDecimal getStateId() {
		return stateId;
	}

	public void setStateId(BigDecimal stateId) {
		this.stateId = stateId;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
}
