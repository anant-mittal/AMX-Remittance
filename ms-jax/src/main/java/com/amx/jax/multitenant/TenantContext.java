package com.amx.jax.multitenant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.scope.TenantContextHolder;

/**
 * 
 * @deprecated use {@link TenantContextHolder}
 *
 */
@Deprecated
public class TenantContext {

	private static Logger logger = LoggerFactory.getLogger(TenantContext.class.getName());

	private static ThreadLocal<String> currentTenant = new ThreadLocal<>();

	public static void setCurrentTenant(String tenant) {
		logger.debug("Setting tenant to " + tenant);
		currentTenant.set(tenant);
	}

	public static String getCurrentTenant() {
		return currentTenant.get();
	}

	public static void clear() {
		currentTenant.set(null);
	}
}