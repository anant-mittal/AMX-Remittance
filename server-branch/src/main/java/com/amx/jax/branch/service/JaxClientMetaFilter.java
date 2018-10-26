package com.amx.jax.branch.service;

import org.springframework.stereotype.Component;

import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.rest.RestMetaRequestOutFilter;
import com.amx.utils.ContextUtil;

@Component
public class JaxClientMetaFilter extends RestMetaRequestOutFilter<JaxMetaInfo> {


	@Override
	public JaxMetaInfo exportMeta() {
		JaxMetaInfo jaxMetaInfo = new JaxMetaInfo();

		jaxMetaInfo.setTraceId(ContextUtil.getTraceId());

		return jaxMetaInfo;
	}

}
