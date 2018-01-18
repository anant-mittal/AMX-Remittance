package com.amx.jax.payment.gateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.payment.PayGServiceCode;
import com.amx.jax.scope.Tenant;

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

	public void register(PayGServiceCode serviceCode, PayGClient service) {
		this.servicesByCode.put(serviceCode.toString().toLowerCase(), service);
	}

	public PayGClient getPayGClient(PayGServiceCode service, Tenant tenant) {
		return this.servicesByCode.get(service.toString().toLowerCase());
	}

	public PayGClient getPayGClient(String service, Tenant tenant) {
		return this.servicesByCode.get(service.toLowerCase());
	}
}