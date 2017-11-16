package com.amx.jax.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.Country;
import com.amx.jax.services.AbstractServiceFactory;
import com.amx.jax.services.KwServiceFactory;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MetaData implements IMetaData {

	private Long countryId;

	@Autowired
	private KwServiceFactory kwFactory;

	/**
	 * this returns service factory object depending on site country
	 */
	@Override
	public AbstractServiceFactory getServiceFactory() {
		if (countryId != null && countryId.equals(Country.KUWAIT.getCountryId())) {
			return kwFactory;
		}
		return null;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
}
