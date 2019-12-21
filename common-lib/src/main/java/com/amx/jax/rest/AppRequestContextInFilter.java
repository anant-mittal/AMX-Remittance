package com.amx.jax.rest;

import com.amx.jax.http.CommonHttpRequest;

public interface AppRequestContextInFilter {
	public void appRequestContextInFilter(CommonHttpRequest localCommonHttpRequest);
}
