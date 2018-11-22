package com.amx.jax.branch.service;

import org.springframework.stereotype.Component;

import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.rest.IMetaRequestOutFilter;
import com.amx.utils.ContextUtil;

@Component
public class JaxClientMetaFilter implements IMetaRequestOutFilter<JaxMetaInfo> {

	@Override
	public JaxMetaInfo exportMeta() {
		JaxMetaInfo jaxMetaInfo = new JaxMetaInfo();
		outFilter(jaxMetaInfo);
		return jaxMetaInfo;
	}

	@Override
	public void outFilter(JaxMetaInfo requestMeta) {
		requestMeta.setTraceId(ContextUtil.getTraceId());
	}

}
