package com.amx.jax.payment.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;

@Component
public class PayGConfig {

	@Value("${knet.callback.url}")
	String serviceCallbackUrl;

	@Value("${app.test.enabled}")
	boolean testEnabled;

	@Autowired
	AppConfig appConfig;

	public String getServiceCallbackUrl() {
		return serviceCallbackUrl + appConfig.getAppPrefix();
	}

	/**
	 * @return the testEnabled
	 */
	public boolean isTestEnabled() {
		return testEnabled;
	}

	/**
	 * @param testEnabled
	 *            the testEnabled to set
	 */
	public void setTestEnabled(boolean testEnabled) {
		this.testEnabled = testEnabled;
	}

}
