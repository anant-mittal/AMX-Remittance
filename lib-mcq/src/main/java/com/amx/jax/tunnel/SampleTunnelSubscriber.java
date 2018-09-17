package com.amx.jax.tunnel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TunnelEvent(topic = SampleTunnelEvents.Names.TEST_TOPIC, queued = true)
public class SampleTunnelSubscriber implements ITunnelSubscriber<Object> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(String channel, Object msg) {
		LOGGER.info("======onMessage1==={} ====  {}", channel, msg);
	}

}