package com.amx.jax.util;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.amx.jax.dict.Country;
import com.amx.jax.dict.Tenant;

@Component
public class CountryUtil {

	public boolean isKuwait(String countryCode) {

		if (StringUtils.isEmpty(countryCode)) {
			return false;
		}
		if (Tenant.KWT.getISO2Code().equals(countryCode)) {
			return true;
		}
		return false;
	}

	public boolean isBahrain(String countryCode) {

		if (StringUtils.isEmpty(countryCode)) {
			return false;
		}
		if (Tenant.BHR.getISO2Code().equals(countryCode)) {
			return true;
		}
		return false;
	}

	public boolean isOman(String countryCode) {

		if (StringUtils.isEmpty(countryCode)) {
			return false;
		}
		if (Tenant.OMN.getISO2Code().equals(countryCode)) {
			return true;
		}
		return false;
	}
}
