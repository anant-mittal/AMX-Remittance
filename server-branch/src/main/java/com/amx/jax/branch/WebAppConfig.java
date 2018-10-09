package com.amx.jax.branch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;

@TenantScoped
@Component
public class WebAppConfig {
	
	@Value("${jax.cdn.url}")
	private String cleanCDNUrl;
	
	public String getCleanCDNUrl() {
		return cleanCDNUrl;
	}

}
