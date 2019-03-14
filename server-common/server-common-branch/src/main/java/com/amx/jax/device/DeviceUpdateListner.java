package com.amx.jax.device;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.ResourceUpdateEvent;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.utils.JsonUtil;

@TunnelEventMapping(topic = AmxTunnelEvents.Names.UPDATE_DEVICE_STATUS, scheme = TunnelEventXchange.TASK_WORKER)
public class DeviceUpdateListner implements ITunnelSubscriber<ResourceUpdateEvent> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private DeviceRequest deviceRequest;

	@Override
	public void onMessage(String channel, ResourceUpdateEvent event) {
		LOGGER.debug("======DeviceUpdateListner:onMessage==={} ====  {}", channel, JsonUtil.toJson(event));
		deviceRequest.unDeviceAuthorized(event.getResourceId());
	}
}
