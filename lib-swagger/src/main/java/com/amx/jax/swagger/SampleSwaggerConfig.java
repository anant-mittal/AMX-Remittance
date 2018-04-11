package com.amx.jax.swagger;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ConditionalOnProperty("app.swagger")
public class SampleSwaggerConfig {

	@Autowired(required = false)
	public Parameter swaggerHeaderParam;
	
	@Autowired(required = false)
	public Parameter swaggerTenantParam;

	@Bean
	public Docket productApi() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.amx.jax"))
				// .paths(regex("/product.*"))
				.build();
		if (swaggerHeaderParam != null) {
			docket.globalOperationParameters(Arrays.asList(swaggerHeaderParam));
		}
		if (swaggerTenantParam != null) {
			docket.globalOperationParameters(Arrays.asList(swaggerTenantParam));
		}

		return docket;
	}

	@SuppressWarnings("unused")
	private ApiInfo metaData() {
		ApiInfo apiInfo = new ApiInfo("AMX UI Server Rest API", "Spring Boot REST API for Online Store", "1.0",
				"Terms of service",
				new Contact("Lalit Tanwar", "https://springframework.guru/about/", "lalit.tanwar@almullagroup.com"),
				"Apache License Version 2.0", "https://www.apache.org/licenses/LICENSE-2.0");
		return apiInfo;
	}
}
