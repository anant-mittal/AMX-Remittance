package com.amx.jax.client.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.amx.jax.client.JaxClientErrorHanlder;
import com.amx.jax.filter.AppClientInterceptor;
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
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder, JaxClientErrorHanlder errorHandler) {
		builder.rootUri("https://localhost.com");
		RestTemplate restTemplate = builder.build();
		restTemplate.setInterceptors(Collections.singletonList(new AppClientInterceptor()));
		restTemplate.setErrorHandler(errorHandler);
		return restTemplate;
	}

}
