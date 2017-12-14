package com.amx.jax.scope;

public class SiteContextService {

	static Site site = null;

	public static Site currentSite() {

		if (site == null) {
			site = Site.NETFLIX;
		}
		return site;
		// Implement this to return the current Site. Can be done effectively
		// using ThreadLocals and Filters or Interceptors. For example, an
		// interceptor could examine the domain name
		// (netflix.com/blockbuster.com/betamaxrules.com) to set the Site on a
		// ThreadLocal.
	}

}
