package com.amx.jax.scope;

import com.amx.jax.dict.Tenant;
import com.amx.utils.ContextUtil;

public class TenantContextHolder {

	public static final String TENANT = "tnt";

	public static void setCurrent(Tenant site) {
		ContextUtil.map().put(TENANT, site);
	}

	public static void setCurrent(String siteId) {
		ContextUtil.map().put(TENANT, fromString(siteId, Tenant.DEFAULT));
	}

	public static void setCurrent(String siteId, Tenant defaultTnt) {
		ContextUtil.map().put(TENANT, fromString(siteId, defaultTnt));
	}

	public static void setDefault() {
		ContextUtil.map().put(TENANT, Tenant.KWT);
	}

	public static Tenant currentSite() {
		Object site = ContextUtil.map().get(TENANT);
		if (site == null) {
			return Tenant.KWT;
		}
		return (Tenant) site;
	}

	public static Tenant fromString(String siteId, Tenant defaultTnt) {
		return Tenant.fromString(siteId, defaultTnt);
	}

}
