package com.amx.jax.swagger;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ConditionalOnProperty("app.swagger")
public class SwaggerConfig {

	@Bean
	public Docket productApi() {
//		Parameter headerParam = new ParameterBuilder().name("meta-info").description("meta-info").defaultValue(
//				"{\"countryId\":91,\"customerId\":5218,\"companyId\":1,\"channel\":\"ONLINE\" , \"countryBranchId\":\"78\", \"tenant\":\"KWT2\"}")
//				.modelRef(new ModelRef("string")).parameterType("header").required(true).build();
//		List<Parameter> globalParams = Arrays.asList(headerParam);
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.amx.jax"))
				// .paths(regex("/product.*"))
				.build();
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
