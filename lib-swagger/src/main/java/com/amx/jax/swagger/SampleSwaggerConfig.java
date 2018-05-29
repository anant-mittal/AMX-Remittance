package com.amx.jax.swagger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amx.jax.AppConstants;
import com.amx.jax.def.MockParamBuilder.MockParam;
import com.amx.jax.dict.Tenant;
import com.amx.jax.scope.TenantContextHolder;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableValues;
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
	public Docket productApi(List<MockParam> mockParams) {
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

		List<Parameter> operationParameters = new ArrayList<Parameter>();
		for (MockParam mockParam : mockParams) {
			AllowableValues allowableValues = null;
			if (mockParam.getValues() != null) {
				allowableValues = new AllowableListValues(mockParam.getValues(), mockParam.getValueType());
			}

			Parameter parameter = new ParameterBuilder().name(mockParam.getName())
					.description(mockParam.getDescription()).defaultValue(mockParam.getDefaultValue())
					.modelRef(new ModelRef("string")).parameterType(mockParam.getType().toString().toLowerCase())
					.allowableValues(allowableValues).required(true).build();
			operationParameters.add(parameter);
		}

		AllowableValues allowableTenants = new AllowableListValues(Tenant.tenantStrings(), TenantContextHolder.TENANT);
		operationParameters.add(new ParameterBuilder().name(TenantContextHolder.TENANT).description("Tenant Country")
				.defaultValue("KWT").modelRef(new ModelRef("string")).parameterType("header")
				.allowableValues(allowableTenants).required(true).build());

		operationParameters.add(new ParameterBuilder().name(AppConstants.TRANX_ID_XKEY).description("Transaction Id")
				.modelRef(new ModelRef("string")).parameterType("header").required(false).build());
		operationParameters.add(new ParameterBuilder().name(AppConstants.TRACE_ID_XKEY).description("Trace Id")
				.modelRef(new ModelRef("string")).parameterType("header").required(false).build());

		docket.globalOperationParameters(operationParameters);
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
