package com.amx.service_provider;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amx.jax.AppConfig;
import com.amx.jax.AppConstants;
import com.amx.jax.swagger.DefaultSwaggerConfig.DocketWrapper;
import com.amx.jax.swagger.MockParamBuilder.MockParam;
import com.amx.utils.UniqueID;

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

@Configuration
public class SwaggerConfigServiceProvider {

	public static final String PARAM_STRING = "string";
	public static final String PARAM_HEADER = "header";
	public static final String SWGGER_SECRET_PARAM = "x-swagger-key";
	public static final String SWGGER_SECRET_VALUE = UniqueID.generateString();

	@Autowired(required = false)
	DocketWrapper docketWrapper;

	@Bean
	public Docket serviceProviderApis(List<MockParam> mockParams) {

		if (docketWrapper != null && docketWrapper.getDocket() != null) {
			return docketWrapper.getDocket();
		}

		Docket docket = new Docket(DocumentationType.SWAGGER_2).groupName("Service Provider").select()
				.apis(RequestHandlerSelectors.basePackage("com.amx.service_provider"))
				// .paths(regex("/product.*"))
				.build();

		List<Parameter> operationParameters = new ArrayList<Parameter>();
		for (MockParam mockParam : mockParams) {
			AllowableValues allowableValues = null;
			if (mockParam.getValues() != null) {
				allowableValues = new AllowableListValues(mockParam.getValues(), mockParam.getValueType());
			}

			Parameter parameter = new ParameterBuilder().name(mockParam.getName())
					.description(mockParam.getDescription()).defaultValue(mockParam.getDefaultValue())
					.modelRef(new ModelRef(PARAM_STRING)).parameterType(mockParam.getType().toString().toLowerCase())
					.allowableValues(allowableValues).required(mockParam.isRequired()).hidden(mockParam.isHidden())
					.build();

			operationParameters.add(parameter);
		}

		operationParameters.add(new ParameterBuilder().name(AppConstants.TRANX_ID_XKEY).description("Transaction Id")
				.defaultValue("TST-1d59nub55kbgg-1d59nub5827sx").modelRef(new ModelRef(PARAM_STRING))
				.parameterType(PARAM_HEADER).required(false).build());
		operationParameters.add(new ParameterBuilder().name(AppConstants.TRACE_ID_XKEY).description("Trace Id")
				.defaultValue("TST-1d59nub55kbgg-1d59nub5827sx").modelRef(new ModelRef(PARAM_STRING))
				.parameterType(PARAM_HEADER).required(false).build());
		docket.globalOperationParameters(operationParameters);
		docket.apiInfo(metaData());
		return docket;
	}

	@Autowired
	AppConfig appConfig;

	private ApiInfo metaData() {
		return new ApiInfo(appConfig.getAppName(),
				appConfig.getAppEnv() + "#" + appConfig.getAppGroup() + "#" + appConfig.getAppId(), "1.0",
				"Terms of service",
				new Contact("Lalit Tanwar", "https://springframework.guru/about/", "lalit.tanwar@almullaexchange.com"),
				"Apache License Version 2.0", "https://www.apache.org/licenses/LICENSE-2.0");
	}
}
