package com.amx.jax.scope;

import java.util.List;

import com.amx.jax.config.ScopedBeanFactory;

public class TenantContext<T> extends ScopedBeanFactory<Tenant, T> {

	public TenantContext(List<T> libs) {
		super(libs);
	}

	@Override
	public Tenant[] getKeys(T lib) {
		TenantSpecific annotation = lib.getClass().getAnnotation(TenantSpecific.class);
		if (annotation != null) {
			return annotation.value();
		}
		return null;
	}

	@Override
	public Tenant getKey() {
		return TenantContextHolder.currentSite();
	}
}
