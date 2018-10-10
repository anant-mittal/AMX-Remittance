package com.amx.jax.rest;

public abstract class RestMetaRequestOutFilter<T> {

	/**
	 * Meta Data Info you want to send with outgoing request
	 * 
	 * @param meta
	 */
	public abstract T exportMeta();

}
