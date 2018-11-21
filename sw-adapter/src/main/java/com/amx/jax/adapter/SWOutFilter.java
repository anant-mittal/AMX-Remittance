package com.amx.jax.adapter;

import org.springframework.stereotype.Component;

import com.amx.jax.device.DeviceMetaInfo;
import com.amx.jax.rest.IMetaRequestOutFilter;

@Component
public class SWOutFilter implements IMetaRequestOutFilter<DeviceMetaInfo> {

	@Override
	public DeviceMetaInfo exportMeta() {
		return null;
	}

	@Override
	public void outFilter(DeviceMetaInfo requestMeta) {
		requestMeta.setTname("Heyaaa");
	}

}
