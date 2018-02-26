package com.amx.jax.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import de.codecentric.boot.admin.config.EnableAdminServer;

@SpringBootApplication
@ComponentScan("com.amx.jax")
@EnableAdminServer
public class AdminServiceApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(AdminServiceApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AdminServiceApplication.class);
	}
}
