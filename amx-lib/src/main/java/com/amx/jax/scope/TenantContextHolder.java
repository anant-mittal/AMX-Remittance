package com.amx.jax.scope;

import com.bootloaderjs.ContextUtil;

public class TenantContextHolder {

	public static final String TENANT = "tnt";

	public static void setDefault(Tenant site) {
		ContextUtil.map().put(TENANT, site);
	}

	public static void setCurrent(String siteId) {
		ContextUtil.map().put(TENANT, fromString(siteId));
	}

	public static void setDefault() {
		ContextUtil.map().put(TENANT, Tenant.KUWAIT);
	}

	public static Tenant currentSite() {
		Object site = ContextUtil.map().get(TENANT);
		if (site == null) {
			return Tenant.KUWAIT;
		}
		return (Tenant) site;
	}

	public static Tenant fromString(String siteId) {
		for (Tenant site : Tenant.values()) {
			if (site.getId().equalsIgnoreCase(siteId)) {
				return site;
			}
		}
		return Tenant.KUWAIT;
	}

}
