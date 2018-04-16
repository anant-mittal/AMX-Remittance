package com.amx.jax.ui;

import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;

@TenantScoped
@Component
public class WebAppConfig {

	@TenantValue("${ui.features}")
	private String[] features;

	public String[] getTenant() {
		return features;
	}

	public String[] getTenantLang() {
		return features;
	}

}
