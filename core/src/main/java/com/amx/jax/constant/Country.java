package com.amx.jax.constant;

public enum Country {

	KUWAIT("KW"), BARAIN("BH"), OMAN("OM");

	public final String countryCode;

	Country(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

}
