package com.amx.jax.tunnel.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;

@TunnelEventMapping(topic = SampleTunnelEventsDict.Names.TEST_TOPIC)
public class SampleTunnelSubscriber implements ITunnelSubscriber<Object> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(String channel, Object msg) {
		LOGGER.info("======onMessage1==={} ====  {}", channel, msg);
	}

}
