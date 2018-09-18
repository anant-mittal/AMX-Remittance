package com.amx.jax.rest;

import com.amx.jax.rest.RestMetaService.RequestRestMeta;
import com.amx.jax.rest.RestMetaService.ResponseRestMeta;

public interface RestMetaFilter {

	/**
	 * Meta Data Info you want to send with outgoing request
	 * 
	 * @param meta
	 */
	public void exportMeta(RequestRestMeta meta);

	/**
	 * Meta Data Info you want to extract from incoming request
	 */
	public void importMeta(ResponseRestMeta meta);

}
