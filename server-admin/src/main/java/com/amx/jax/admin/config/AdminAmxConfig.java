package com.amx.jax.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;

@Configuration
public class AdminAmxConfig {

	@Bean
	public Parameter swaggerHeaderParam() {
		return new ParameterBuilder().name("meta-info").description("meta-info").defaultValue(
				"{\"countryId\":91,\"customerId\":5218,\"companyId\":1,\"channel\":\"ONLINE\" , \"countryBranchId\":\"78\", \"tenant\":\"KWT2\"}")
				.modelRef(new ModelRef("string")).parameterType("header").required(true).build();
	}

}
