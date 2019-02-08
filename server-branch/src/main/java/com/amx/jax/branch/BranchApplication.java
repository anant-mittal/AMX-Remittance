package com.amx.jax.branch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.client.configs.JaxMetaInfo;

@SpringBootApplication
@ComponentScan("com.amx.jax")
@EnableAsync(proxyTargetClass = true)
public class BranchApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BranchApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BranchApplication.class);
	}

	@Bean
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public JaxMetaInfo jaxMetaInfo() {
		return new JaxMetaInfo();
	}

}
