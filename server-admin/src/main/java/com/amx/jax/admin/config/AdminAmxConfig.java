package com.amx.jax.admin.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amx.jax.dict.Tenant;
import com.amx.jax.scope.TenantContextHolder;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.service.Parameter;

@Configuration
public class AdminAmxConfig {

	@Bean
	public Parameter swaggerHeaderParam() {
		return new ParameterBuilder().name("meta-info").description("meta-info").defaultValue(
				"{\"countryId\":91,\"customerId\":5218,\"companyId\":1,\"channel\":\"ONLINE\" , \"countryBranchId\":\"78\", \"tenant\":\"KWT2\"}")
				.modelRef(new ModelRef("string")).parameterType("header").required(true).build();
	}

	@Bean
	public Parameter swaggerTenantParam() {
		List<String> values = new ArrayList<>();
		values.add(Tenant.KWT.toString());
		values.add(Tenant.BHR.toString());
		values.add(Tenant.OMN.toString());
		AllowableValues allowableValues = new AllowableListValues(values, TenantContextHolder.TENANT);
		return new ParameterBuilder().name(TenantContextHolder.TENANT).description("Country").defaultValue("KWT")
				.modelRef(new ModelRef("string")).parameterType("header").allowableValues(allowableValues)
				.required(true).build();
	}

}
