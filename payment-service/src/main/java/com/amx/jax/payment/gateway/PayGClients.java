package com.amx.jax.payment.gateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.payment.gateway.PayGClient.Services;
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

	public void register(Services serviceCode, PayGClient service) {
		this.servicesByCode.put(serviceCode.toString().toLowerCase(), service);
	}

	public PayGClient getPayGClient(Services services, Tenant tenant) {
		return this.servicesByCode.get(services.toString().toLowerCase());
	}

	public PayGClient getPayGClient(PayGParams payGParams) {
		return this.getPayGClient(payGParams.getService(), payGParams.getCountryCode());
	}
}