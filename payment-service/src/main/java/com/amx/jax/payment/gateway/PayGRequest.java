package com.amx.jax.payment.gateway;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.amx.jax.payment.gateway.PayGClient.Services;
import com.amx.jax.scope.Tenant;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PayGRequest {

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
