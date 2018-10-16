package com.amx.jax.scope;

import com.amx.jax.dict.Tenant;

public abstract class AbstractTenantService {

	public Tenant getTenant() {
		return TenantContextHolder.currentSite();
	}
	
}