package com.amx.jax.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.amx.jax")
public class JaxServiceClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(JaxServiceClientApplication.class, args);
	}

}
