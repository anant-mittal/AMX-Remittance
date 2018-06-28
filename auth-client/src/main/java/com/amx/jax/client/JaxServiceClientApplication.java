package com.amx.jax.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.amx.jax.client.config.JaxConfig;

@SpringBootApplication
@ComponentScan(basePackages = "com.amx.jax")
public class JaxServiceClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(JaxServiceClientApplication.class, args);
	}

	@Autowired
	protected JaxConfig jaxConfig;

}
