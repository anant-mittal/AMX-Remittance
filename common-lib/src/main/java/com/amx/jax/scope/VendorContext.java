package com.amx.jax.scope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.amx.common.ScopedBeanFactory;
import com.amx.jax.AppConstants;
import com.amx.jax.swagger.ApiMockParam;
import com.amx.jax.swagger.ApiMockParams;
import com.amx.jax.swagger.MockParamBuilder.MockParamType;

public class VendorContext<T> extends ScopedBeanFactory<String, T> {

	private static final long serialVersionUID = 4007091611441725719L;

	@Target({ ElementType.TYPE, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	@ApiMockParams({
			@ApiMockParam(name = AppConstants.AUTH_ID_XKEY, value = "Vendor Id", 
					defaultValue = "vendor", paramType = MockParamType.HEADER),
			@ApiMockParam(name = AppConstants.AUTH_TOKEN_XKEY, value = "Vendor Request Token", 
			defaultValue = "vendor@1234", paramType = MockParamType.HEADER) })
	public @interface ApiVendorHeaders {

	}

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
