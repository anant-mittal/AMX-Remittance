package com.amx.jax;

import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;

@TenantScoped
@Component
public class JaxClientConfig {

	@TenantValue("${tenant}")
	private String tenant;

}
