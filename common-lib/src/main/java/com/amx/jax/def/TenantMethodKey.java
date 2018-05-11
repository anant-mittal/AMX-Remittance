package com.amx.jax.def;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;

@Component
public class TenantMethodKey implements KeyGenerator {

	public static final String KEY = "tenantMethodKey";

	@Override
	public Object generate(Object target, Method method, Object... params) {
		StringBuilder b = new StringBuilder(
				AppContextUtil.getTenant() + "#" + target.getClass().getName() + "#" + method.getName());
		if (params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				b.append("#" + params[i]);
			}
		}
		return b.toString();
	}

}