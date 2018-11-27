package com.amx.jax.device;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;
import com.amx.utils.ArgUtil;

@Component
public class DeviceBox extends CacheBox<DeviceData> {

	@Override
	public DeviceData getDefault() {
		return new DeviceData();
	}

	/**
	 * 
	 * This method is requried to called whenever there is change in status/state of
	 * device
	 * 
	 * @param deviceRegid
	 */
	public void updateStamp(Object deviceRegid) {
		if (!ArgUtil.isEmpty(deviceRegid)) {
			String deviceRegidStr = ArgUtil.parseAsString(deviceRegid);
			DeviceData deviceData = this.getOrDefault(deviceRegidStr);
			deviceData.setUpdatestamp(System.currentTimeMillis());
			this.fastPut(deviceRegidStr, deviceData);
		}
	}
}