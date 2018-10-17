package com.amx.jax.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.amx.jax")
public class JaxServiceClientMain {

	public static void main(String[] args) {
		SpringApplication.run(JaxServiceClientMain.class, args);
	}

	@Bean
	public com.amx.jax.client.configs.JaxMetaInfo JaxMetaInfo() {
		com.amx.jax.client.configs.JaxMetaInfo metaInfo = new com.amx.jax.client.configs.JaxMetaInfo();
		return metaInfo;
	}
}
