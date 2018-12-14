package com.amx.jax.swagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amx.jax.AppConfig;
import com.amx.jax.AppConstants;
import com.amx.jax.dict.Tenant;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.swagger.MockParamBuilder.MockParam;
import com.amx.utils.CollectionUtil;
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
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ConditionalOnProperty("app.swagger")
public class DefaultSwaggerConfig {

	public static final String PARAM_STRING = "string";
	public static final String PARAM_HEADER = "header";
	public static final String SWGGER_SECRET_PARAM = "x-swagger-key";
	public static final String SWGGER_SECRET_VALUE = UniqueID.generateString();

	@Bean
	public Docket productApi(List<MockParam> mockParams) {
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.amx.jax"))
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
				.defaultValue("TST-1d59nub55kbgg-1d59nub5827sx")
				.modelRef(new ModelRef(PARAM_STRING)).parameterType(PARAM_HEADER).required(false).build());
		operationParameters.add(new ParameterBuilder().name(AppConstants.TRACE_ID_XKEY).description("Trace Id")
				.defaultValue("TST-1d59nub55kbgg-1d59nub5827sx")
				.modelRef(new ModelRef(PARAM_STRING)).parameterType(PARAM_HEADER).required(false).build());
		docket.globalOperationParameters(operationParameters);
		docket.apiInfo(metaData());
		return docket;
	}

	@Bean
	public MockParam tenantParam() {
		return new MockParamBuilder().name(TenantContextHolder.TENANT).description("Tenant Country").defaultValue("KWT")
				.parameterType(MockParamBuilder.MockParamType.HEADER)
				.allowableValues(Tenant.tenantStrings(), TenantContextHolder.TENANT).required(true).build();

	}

	@Bean
	public MockParam swaggerParam() {
		return new MockParamBuilder().name(SWGGER_SECRET_PARAM).description(SWGGER_SECRET_PARAM)
				.defaultValue(SWGGER_SECRET_VALUE)
				.parameterType(MockParamBuilder.MockParamType.HEADER)
				.allowableValues(CollectionUtil.getList(SWGGER_SECRET_VALUE), PARAM_STRING).required(true)
				.hidden(true)
				.build();

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
