package com.amx.jax.device;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.ResourceUpdateEvent;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;

@TunnelEventMapping(topic = AmxTunnelEvents.Names.UPDATE_DEVICE_STATUS, scheme = TunnelEventXchange.SHOUT_LISTNER)
public class DeviceUpdateListner implements ITunnelSubscriber<ResourceUpdateEvent> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private DeviceRequest deviceRequest;

	@Override
	public void onMessage(String channel, ResourceUpdateEvent event) {
		LOGGER.debug("======ResourceUpdateEvent({} ,{})", event.getEventType(), event.getResourceId());
		deviceRequest.unDeviceAuthorized(event.getResourceId());
	}
}
