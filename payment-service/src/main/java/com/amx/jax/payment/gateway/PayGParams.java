package com.amx.jax.payment.gateway;

import com.amx.jax.payment.gateway.PayGClient.Services;
import com.amx.jax.scope.Tenant;

public class PayGParams {

	private Services service;

	public Services getService() {
		return service;
	}

	public void setService(Services service) {
		this.service = service;
	}

	private Tenant countryCode;

	public Tenant getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(Tenant countryCode) {
		this.countryCode = countryCode;
	}

}
