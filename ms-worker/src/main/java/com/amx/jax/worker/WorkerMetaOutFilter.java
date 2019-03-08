package com.amx.jax.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.constants.JaxChannel;
import com.amx.jax.rest.IMetaRequestOutFilter;

@Primary
@Component
public class WorkerMetaOutFilter implements IMetaRequestOutFilter<JaxMetaInfo> {

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Override
	public JaxMetaInfo exportMeta() {
		JaxMetaInfo requestMeta = jaxMetaInfo.copy();
		outFilter(requestMeta);
		return requestMeta;
	}

	@Override
	public void outFilter(JaxMetaInfo requestMeta) {
		requestMeta.setChannel(JaxChannel.SYSTEM);
	}

}
