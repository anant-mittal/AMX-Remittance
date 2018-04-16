package com.amx.jax.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amx.jax.client.JaxClientErrorHanlder;
import com.amx.jax.rest.RestService;

@Configuration
public class JaxConfig {

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

	@Bean
	public RestService jaxRestService(RestService restService, JaxClientErrorHanlder errorHandler) {
		restService.setErrorHandler(errorHandler);
		return restService;
	}

}
