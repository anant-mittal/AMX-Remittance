package com.amx.jax.constant;

public enum Country {

	KUWAIT(91L);

	public final Long countryId;

	Country(Long countryId) {
		this.countryId = countryId;
	}

	public Long getCountryId() {
		return countryId;
	}
}
