package com.amx.jax.client;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import com.amx.jax.client.config.JaxConfig;

@SpringBootApplication
@ComponentScan(basePackages = "com.amx.jax")
public class JaxServiceClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(JaxServiceClientApplication.class, args);
	}

	@Autowired
	protected JaxConfig jaxConfig;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder, JaxClientErrorHanlder errorHandler) {
		builder.rootUri(jaxConfig.getSpServiceUrl());
		RestTemplate restTemplate = builder.build();
		restTemplate.setErrorHandler(errorHandler);
		return restTemplate;
	}

	@Bean("base_url")
	public URL baseUrl() throws MalformedURLException {
		return new URL(jaxConfig.getSpServiceUrl());
	}

}
