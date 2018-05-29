package com.amx.jax.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

@SpringBootApplication
@ComponentScan("com.amx.jax")
public class PaymentServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PaymentServiceApplication.class);
    }
    
    @Bean
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public com.amx.jax.amxlib.model.JaxMetaInfo JaxMetaInfo() {
		com.amx.jax.amxlib.model.JaxMetaInfo metaInfo = new com.amx.jax.amxlib.model.JaxMetaInfo();
		return metaInfo;
	}
}
