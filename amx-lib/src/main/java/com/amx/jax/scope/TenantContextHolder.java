package com.amx.jax.scope;

import com.bootloaderjs.ContextUtil;

public class TenantContextHolder {

	public static final String TENANT = "tnt";

	public static void setCurrent(TenantContextHolder context) {
		ContextUtil.map().put(TENANT, context.getSite());
	}

	public static Tenant currentSite() {
		return (Tenant) ContextUtil.map().get(TENANT);
	}

	Tenant site;

	public TenantContextHolder() {
		this.site = Tenant.KUWAIT;
	}

	public TenantContextHolder(String siteId) {
		this.site = fromString(siteId);
	}

	public Tenant getSite() {
		return site;
	}

	public void setSite(Tenant site) {
		this.site = site;
	}

	public void setSite(String siteId) {
		this.site = fromString(siteId);
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
