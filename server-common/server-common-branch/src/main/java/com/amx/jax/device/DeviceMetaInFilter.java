package com.amx.jax.device;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.amx.jax.device.DeviceMetaInfo;
import com.amx.jax.rest.IMetaRequestInFilter;

@Component
public class DeviceMetaInFilter implements IMetaRequestInFilter<DeviceMetaInfo> {

	@Override
	public Class<DeviceMetaInfo> getMetaClass() {
		return DeviceMetaInfo.class;
	}

	@Override
	public void importMeta(DeviceMetaInfo meta, HttpServletRequest req) {
		// System.out.println("importMeta " + meta.getTname());
	}

	@Override
	public void inFilter(DeviceMetaInfo requestMeta) {
		// System.out.println("inFilter " + requestMeta.getTname());
	}

}
