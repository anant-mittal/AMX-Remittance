package com.amx.jax.ui.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Properties {

	@Value("${jaxservice.url}")
	private String jaxServiceUrl;

	public String getJaxServiceUrl() {
		return jaxServiceUrl;
	}

	public void setJaxServiceUrl(String jaxServiceUrl) {
		this.jaxServiceUrl = jaxServiceUrl;
	}

}
