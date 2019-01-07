package com.amx.jax;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;
import com.amx.jax.swagger.MockParamBuilder;
import com.amx.jax.swagger.MockParamBuilder.MockParam;

@TenantScoped
@Component
public class JaxClientConfig {

	@TenantValue("${tenant}")
	private String tenant;

	@Bean
	public MockParam basicParam() {
		return new MockParamBuilder().name(AppConstants.REQUEST_PARAMS_XKEY)
				.description(AppConstants.REQUEST_PARAMS_XKEY)
				.defaultValue("{\"mOtp\":123456,\"eOtp\":234567,\"secAns\":\"black\"}")
				.parameterType(MockParamBuilder.MockParamType.HEADER).required(true).build();
	}
}
