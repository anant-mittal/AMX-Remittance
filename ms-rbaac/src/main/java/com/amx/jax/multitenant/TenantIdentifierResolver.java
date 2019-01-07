package com.amx.jax.multitenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantContextHolder;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

	@Override
	public String resolveCurrentTenantIdentifier() {
		String tenantId = TenantContextHolder.currentSite().toString();
		if (tenantId != null) {
			return tenantId;
		}
		return "KWT";
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}
}
