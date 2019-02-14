package com.amx.jax.broker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.dict.Tenant;
import com.amx.jax.scope.TenantScoped;

@Component
@TenantScoped
public class BrokerConfig {

	@Value("${broker.service.tenants}")
	Tenant[] tenants;

	public Tenant[] getTenants() {
		return tenants;
	}
}
