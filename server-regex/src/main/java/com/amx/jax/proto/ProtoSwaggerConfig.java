package com.amx.jax.proto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amx.jax.AppConstants;

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
public class ProtoSwaggerConfig {

	public static final String PARAM_STRING = "string";
	public static final String PARAM_HEADER = "header";

	@Bean
	public Docket productApi() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.amx.jax"))
				// .paths(regex("/product.*"))
				.build();

		List<Parameter> operationParameters = new ArrayList<Parameter>();

		operationParameters.add(
				new ParameterBuilder().name(AppConstants.TRACE_ID_XKEY).description("Trace Id")
						.modelRef(new ModelRef(PARAM_STRING)).parameterType(PARAM_HEADER).required(false).build());

		docket.globalOperationParameters(operationParameters);
		docket.apiInfo(metaData());
		return docket;
	}

	private ApiInfo metaData() {
		return new ApiInfo(
				"AMSService",
				"Third Party Client Server", "1.0",
				"Terms of service",
				new Contact("", "https://modernexchange.com/", "tech@almullaexchange.com"),
				"Apache License Version 2.0", "https://www.apache.org/licenses/LICENSE-2.0");
	}
}
