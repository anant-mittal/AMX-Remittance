package com.amx.jax.payment.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaseServices {

	private final Map<String, PayGClient> servicesByCountryCode = new HashMap<>();

	@Autowired
	public CaseServices(List<PayGClient> services) {
		for (PayGClient service : services) {
			register(service.getClientName(), service);
		}
	}

	public void register(String countryCode, PayGClient service) {
		this.servicesByCountryCode.put(countryCode, service);
	}

	public PayGClient getCaseService(String countryCode) {
		return this.servicesByCountryCode.get(countryCode);
	}
}