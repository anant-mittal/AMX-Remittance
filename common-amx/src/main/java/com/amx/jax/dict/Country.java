package com.amx.jax.dict;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @deprecated For Tenants use {@link com.amx.jax.dict.Tenant} and for countries
 *             {@link com.amx.jax.dict.Countries}
 * 
 * @author lalittanwar
 *
 */
@Deprecated
public enum Country {

	KUWAIT("KW", 91), BARAIN("BH", 104), OMAN("OM", 82);

	public final String countryCode;

	public final Integer countryId;

	Country(String countryCode, Integer countryId) {
		this.countryCode = countryCode;
		this.countryId = countryId;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public static final Map<Integer, Country> countryIdToCountryMap = new HashMap<>();
	static {

		for (Country c : Country.values()) {
			countryIdToCountryMap.put(c.getCountryId(), c);
		}
	}
}
