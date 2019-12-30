package com.amx.jax.tunnel.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.cache.test.RedisSampleCacheBox.RedisSampleData;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.utils.JsonUtil;

@TunnelEventMapping(topic = SampleTunnelEventsDict.Names.TEST_TOPIC)
public class SampleTunnelSubscriber implements ITunnelSubscriber<RedisSampleData> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(String channel, RedisSampleData msg) {
		LOGGER.info("======onMessage1==={} ====  {}", channel,
				JsonUtil.toJson(msg));
	}

}
