package com.amx.jax.scope;

public abstract class AbstractTenantService {

	public Tenant getTenant() {
		return TenantContextHolder.currentSite();
	}
	
}