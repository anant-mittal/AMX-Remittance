package com.amx.jax.tunnel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TunnelEvent(topic = TunnelClient.TEST_TOPIC)
public class SampleTunnelSubscriber implements ITunnelSubscriber<String> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(String channel, String msg) {
		LOGGER.info("======onMessage==={}", msg);
	}

}