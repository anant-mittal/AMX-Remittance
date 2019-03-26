package com.amx.jax;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.amx.jax.JaxAuthCache.JaxAuthMeta;
import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;
import com.amx.jax.swagger.MockParamBuilder;
import com.amx.jax.swagger.MockParamBuilder.MockParam;
import com.amx.utils.JsonUtil;

@TenantScoped
@Component
public class JaxClientConfig {

	@TenantValue("${tenant}")
	private String tenant;

	@Bean
	public MockParam basicParam() {
		return new MockParamBuilder().name(AppConstants.REQUEST_PARAMS_XKEY)
				.description(AppConstants.REQUEST_PARAMS_XKEY)
				.defaultValue(JsonUtil.toJson(new JaxAuthMeta("", "", "")))
				.parameterType(MockParamBuilder.MockParamType.HEADER).required(true).build();
	}
}
