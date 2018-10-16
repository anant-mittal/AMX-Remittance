package com.amx.jax.offsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.client.configs.JaxMetaInfo;

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

	@Bean
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public JaxMetaInfo jaxMetaInfo() {
		return new JaxMetaInfo();
	}

}
