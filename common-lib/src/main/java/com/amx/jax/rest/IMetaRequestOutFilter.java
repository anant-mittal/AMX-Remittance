package com.amx.jax.rest;

public interface IMetaRequestOutFilter<T extends RequestMetaInfo> {

	/**
	 * Meta Data Info you want to send with outgoing request
	 * 
	 * @param meta
	 */
	public T exportMeta();

	public void outFilter(T requestMeta);

}
