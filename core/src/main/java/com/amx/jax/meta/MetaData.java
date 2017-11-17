package com.amx.jax.meta;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.services.AbstractServiceFactory;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MetaData implements IMetaData {

	private Long countryId;

	/**
	 * this returns service factory object depending on site country
	 */
	@Override
	public AbstractServiceFactory getServiceFactory() {
		
		return null;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
}
