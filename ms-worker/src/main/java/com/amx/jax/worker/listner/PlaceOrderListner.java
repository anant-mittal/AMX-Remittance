package com.amx.jax.worker.listner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.event.Event;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEvent;
import com.amx.utils.JsonUtil;

@TunnelEvent(topic = AmxTunnelEvents.Names.XRATE_BEST_RATE_CHANGE, queued = true)
public class PlaceOrderListner implements ITunnelSubscriber<Event> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(String channel, Event event) {
		LOGGER.info("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));
	}

}