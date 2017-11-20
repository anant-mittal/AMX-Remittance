package com.amx.jax.meta;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.Country;
import com.amx.jax.services.AbstractServiceFactory;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MetaData implements IMetaData {

	private Integer countryId;
	
	private Integer languageId;

	public Integer getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	/**
	 * this returns service factory object depending on site country
	 */
	@Override
	public AbstractServiceFactory getServiceFactory() {

		return null;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public Country getCountry() {
		Country country = Country.countryIdToCountryMap.get(countryId);
		return country;
	}
}
