package com.amx.jax.rest;

import javax.servlet.http.HttpServletRequest;

import com.amx.utils.JsonUtil;

public interface IMetaRequestInFilter<T extends RequestMetaInfo> {

	public Class<T> getMetaClass();

	default T export(String metaString) {
		return JsonUtil.fromJson(metaString, getMetaClass());
	}

	/**
	 * Meta Data Info you want to extract from incoming request
	 * 
	 * @param req
	 * @throws Exception
	 */
	public void importMeta(T meta, HttpServletRequest req);

	public void inFilter(T requestMeta);

}
