package com.amx.jax.offsite.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.amx.jax.rest.IRestMetaFilter;

@Component
public class JaxClientMetaFilter2 implements IRestMetaFilter<Object> {

	@Override
	public Object exportMeta() {
		return null;
	}

	@Override
	public void importMeta(Object meta, HttpServletRequest req) throws Exception {
		// meta.setDeviceIp("TODO");
	}

}
