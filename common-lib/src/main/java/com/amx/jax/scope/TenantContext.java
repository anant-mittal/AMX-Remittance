package com.amx.jax.scope;

import java.util.List;

import com.amx.common.ScopedBeanFactory;
import com.amx.jax.dict.Tenant;

public class TenantContext<T> extends ScopedBeanFactory<Tenant, T> {

	private static final long serialVersionUID = 4007091611441725719L;

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
