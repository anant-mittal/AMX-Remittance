package com.amx.jax.payment.gateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.payment.gateway.PayGClient.Services;

/**
 * 
 * @author lalittanwar
 *
 */
@Service
public class PayGClients {

	private final Map<String, PayGClient> servicesByCode = new HashMap<>();

	@Autowired
	public PayGClients(List<PayGClient> services) {
		for (PayGClient service : services) {
			register(service.getClientCode(), service);
		}
	}

	public void register(Services clientCode, PayGClient service) {
		this.servicesByCode.put(clientCode.toString().toLowerCase(), service);
	}

	public PayGClient getpayGClient(String clientCode, String countryCode) {
		return this.servicesByCode.get(clientCode.toLowerCase());
	}
}