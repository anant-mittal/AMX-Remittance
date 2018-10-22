package com.amx.jax.client.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.client.AuthClientErrorHanlder;
import com.amx.jax.filter.AppClientInterceptor;
import com.amx.jax.rest.RestService;

@Configuration
public class AuthConfig {

	@Value("${authservice.url}")
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
	public RestService jaxRestService(RestService restService, AuthClientErrorHanlder errorHandler) {
		restService.setErrorHandler(errorHandler);
		return restService;
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder, AuthClientErrorHanlder errorHandler) {
		builder.rootUri("https://localhost.com");
		RestTemplate restTemplate = builder.build();
		restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
		restTemplate.setInterceptors(Collections.singletonList(new AppClientInterceptor()));
		restTemplate.setErrorHandler(errorHandler);
		return restTemplate;
	}
	
	@Bean
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public com.amx.jax.amxlib.model.JaxMetaInfo JaxMetaInfo() {
		com.amx.jax.amxlib.model.JaxMetaInfo metaInfo = new com.amx.jax.amxlib.model.JaxMetaInfo();
		return metaInfo;
	}
}
