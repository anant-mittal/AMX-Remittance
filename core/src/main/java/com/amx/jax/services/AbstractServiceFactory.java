package com.amx.jax.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.meta.MetaData;

public abstract class AbstractServiceFactory {

	@Autowired
	protected MetaData metaData;

	public abstract AbstractService getUserService();

	public abstract AbstractService getCustomerService();
	
	public abstract AbstractService getCountryService();

}
