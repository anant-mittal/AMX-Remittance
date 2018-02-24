package com.amx.jax.client.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.client.AbstractJaxServiceClient;

@Component
public class JaxConfig {

	private Logger log = Logger.getLogger(AbstractJaxServiceClient.class);

	@Value("${jaxservice.url}")
	private String spServiceUrl;

	public String getSpServiceUrl() {
		return spServiceUrl;
	}

	public String getDefaultUrl() {
		return "http://jax.modernexchange.com";
	}

	public void setSpServiceUrl(String spServiceUrl) {
		this.spServiceUrl = spServiceUrl;
	}

}
