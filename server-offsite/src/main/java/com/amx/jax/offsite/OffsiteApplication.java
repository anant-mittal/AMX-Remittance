package com.amx.jax.offsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * @author lalittanwar
 *
 */
@SpringBootApplication
@ComponentScan("com.amx.jax")
public class OffsiteApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(OffsiteApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(OffsiteApplication.class);
	}

}
