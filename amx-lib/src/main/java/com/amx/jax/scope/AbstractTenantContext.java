package com.amx.jax.scope;

public abstract class AbstractTenantContext {

	public Tenant getTenant() {
		return TenantContextHolder.currentSite();
	}
	
}