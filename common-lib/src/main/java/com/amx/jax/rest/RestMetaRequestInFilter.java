package com.amx.jax.rest;

import javax.servlet.http.HttpServletRequest;

import com.amx.utils.JsonUtil;

public abstract class RestMetaRequestInFilter<T> {

	public abstract Class<T> getMetaClass();

	public T export(String metaString) {
		return JsonUtil.fromJson(metaString, getMetaClass());
	}

	/**
	 * Meta Data Info you want to extract from incoming request
	 * 
	 * @param req
	 * @throws Exception
	 */
	public abstract void importMeta(T meta, HttpServletRequest req) throws Exception;

}
