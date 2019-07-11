package com.amx.jax.payment.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;

@Component
public class PayGConfig {

	@Value("${knet.callback.url}")
	String serviceCallbackUrl;

	@Autowired
	AppConfig appConfig;

	public String getServiceCallbackUrl() {
		return serviceCallbackUrl + appConfig.getAppPrefix();
	}

}
