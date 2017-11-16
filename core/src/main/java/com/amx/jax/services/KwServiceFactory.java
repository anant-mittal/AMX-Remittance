package com.amx.jax.services;

import org.springframework.stereotype.Component;

@Component
public class KwServiceFactory extends AbstractServiceFactory {

	private AbstractService userService;

	private AbstractService custService;
	
	private AbstractService countryService;

	public void setCountryService(AbstractService countryService) {
		this.countryService = countryService;
	}

	@Override
	public AbstractService getUserService() {
		return userService;
	}

	@Override
	public AbstractService getCustomerService() {
		return custService;
	}

	public void setCustService(AbstractService custService) {
		this.custService = custService;
	}

	public void setUserService(AbstractService userService) {
		this.userService = userService;
	}

	@Override
	public AbstractService getCountryService() {
		return this.countryService;
	}

	
}
