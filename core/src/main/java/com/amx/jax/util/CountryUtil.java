package com.amx.jax.util;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.amx.jax.dict.Country;

@Component
public class CountryUtil {

	public boolean isKuwait(String countryCode) {

		if (StringUtils.isEmpty(countryCode)) {
			return false;
		}
		if (Country.KUWAIT.getCountryCode().equals(countryCode)) {
			return true;
		}
		return false;
	}

	public boolean isBahrain(String countryCode) {

		if (StringUtils.isEmpty(countryCode)) {
			return false;
		}
		if (Country.BARAIN.getCountryCode().equals(countryCode)) {
			return true;
		}
		return false;
	}

	public boolean isOman(String countryCode) {

		if (StringUtils.isEmpty(countryCode)) {
			return false;
		}
		if (Country.OMAN.getCountryCode().equals(countryCode)) {
			return true;
		}
		return false;
	}
}
