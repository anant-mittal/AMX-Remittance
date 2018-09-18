package com.amx.jax.rest;

public interface RestMetaRequestOutFilter<T> {

	/**
	 * Meta Data Info you want to send with outgoing request
	 * 
	 * @param meta
	 */
	public T exportMeta();

}
