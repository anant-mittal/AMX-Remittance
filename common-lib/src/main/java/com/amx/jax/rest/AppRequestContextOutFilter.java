package com.amx.jax.rest;

import org.springframework.http.HttpRequest;

public interface AppRequestContextOutFilter {
	public void appRequestContextOutFilter(HttpRequest request);
}
