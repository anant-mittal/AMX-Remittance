package com.amx.jax;

import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;

@Component
@TenantScoped
public class AppTenantConfig {

	@TenantValue("${encrypted.tenant.property}")
	String tenantSpecifcDecryptedProp;

	public String getTenantSpecifcDecryptedProp() {
		return tenantSpecifcDecryptedProp;
	}

	@TenantValue("${encrypted.tenant.property2}")
	String tenantSpecifcDecryptedProp2;

	public String getTenantSpecifcDecryptedProp2() {
		return tenantSpecifcDecryptedProp2;
	}
}
