package com.amx.jax.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.amx.common.ScopedBeanFactory;
import com.amx.jax.AppConstants;

public class VendorContext<T> extends ScopedBeanFactory<String, T> {

	private static final long serialVersionUID = 4007091611441725719L;

	@Qualifier
	@Scope(value = AppConstants.Scopes.VENDOR, proxyMode = ScopedProxyMode.TARGET_CLASS)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface VendorScoped {
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface VendorValue {
		String value();
	}

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
