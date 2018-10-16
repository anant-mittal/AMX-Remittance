package com.amx.jax.payment.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PayGConfig {

	@Value("${knet.callback.url}")
	String serviceCallbackUrl;

	public String getServiceCallbackUrl() {
		return serviceCallbackUrl;
	}

}
