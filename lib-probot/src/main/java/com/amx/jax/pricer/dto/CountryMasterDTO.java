package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class CountryMasterDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private BigDecimal countryId;
	private String countryCode;
	private String countryAlpha2Code;
	private String countryAlpha3Code;

	// private List<CountryMasterDesc> fsCountryMasterDescs = new
	// ArrayList<CountryMasterDesc>();
	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryAlpha2Code() {
		return countryAlpha2Code;
	}

	public void setCountryAlpha2Code(String countryAlpha2Code) {
		this.countryAlpha2Code = countryAlpha2Code;
	}

	public String getCountryAlpha3Code() {
		return countryAlpha3Code;
	}

	public void setCountryAlpha3Code(String countryAlpha3Code) {
		this.countryAlpha3Code = countryAlpha3Code;
	}

}
