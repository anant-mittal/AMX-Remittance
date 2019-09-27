package com.amx.jax.scope;

import java.util.List;

import com.amx.common.ScopedBeanFactory;
import com.amx.jax.dict.Tenant;

public class VendorContext<T> extends ScopedBeanFactory<String, T> {

	private static final long serialVersionUID = 4007091611441725719L;

	public VendorContext(List<T> libs) {
		super(libs);
	}

	@Override
	public String[] getKeys(T lib) {
		VendorScoped annotation = lib.getClass().getAnnotation(VendorScoped.class);
		if (annotation != null) {
			return null;
		}
		return null;
	}

	@Override
	public String getKey() {
		return null;
	}
}
