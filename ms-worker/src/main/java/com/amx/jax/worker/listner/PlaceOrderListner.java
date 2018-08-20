package com.amx.jax.worker.listner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEvent;

@TunnelEvent(topic = AmxTunnelEvents.Names.XRATE_BEST_RATE_CHANGE, queued = true)
public class PlaceOrderListner implements ITunnelSubscriber<Object> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(String channel, Object msg) {
		LOGGER.info("======onMessage1==={} ====  {}", channel, msg);
	}

}