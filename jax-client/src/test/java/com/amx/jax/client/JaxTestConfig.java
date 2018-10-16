package com.amx.jax.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class JaxTestConfig {

	@Bean
	public com.amx.jax.client.configs.JaxMetaInfo jaxMetaInfo() {
		return new com.amx.jax.client.configs.JaxMetaInfo();
	}
}
