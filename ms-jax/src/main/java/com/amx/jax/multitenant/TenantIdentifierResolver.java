package com.amx.jax.multitenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

import com.amx.jax.dict.Tenant;
import com.amx.jax.scope.TenantContextHolder;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

	@Override
	public String resolveCurrentTenantIdentifier() {
		Tenant tenant = TenantContextHolder.currentSite();
		if (tenant != null) {
			return tenant.toString();
		}
		return Tenant.KWT.toString();
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}
}
