package com.amx.jax.client;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.amx.jax.filter.AppClientErrorHanlder;
import com.amx.jax.filter.AppClientInterceptor;
import com.amx.jax.rest.RestService;

@Profile("test")
@Configuration
public class JaxTestConfig {

	@Bean
	public com.amx.jax.client.configs.JaxMetaInfo jaxMetaInfo() {
		return new com.amx.jax.client.configs.JaxMetaInfo();
	}
}
