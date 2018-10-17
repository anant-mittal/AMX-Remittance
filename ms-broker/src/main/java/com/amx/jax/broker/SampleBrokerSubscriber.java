package com.amx.jax.broker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.utils.JsonUtil;

@TunnelEventMapping(topic = "DB_EVENT")
public class SampleBrokerSubscriber implements ITunnelSubscriber<Object> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(String channel, Object msg) {
		LOGGER.info("======onMessage2==={} ====  {}", channel, JsonUtil.toJson(msg));
	}

}