package com.amx.jax.rest;

import javax.servlet.http.HttpServletRequest;

public interface IRestMetaFilter<T> {

	/**
	 * Meta Data Info you want to send with outgoing request
	 * 
	 * @param meta
	 */
	public T exportMeta();

	/**
	 * Meta Data Info you want to extract from incoming request
	 * 
	 * @param req
	 * @throws Exception
	 */
	public void importMeta(T meta, HttpServletRequest req) throws Exception;

}
