package com.amx.jax.cache.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.cache.test.RedisSampleTxCacheBox.RedisSampleData;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.sys.SysTunnelEventsDict;
import com.amx.utils.JsonUtil;

@TunnelEventMapping(topic = SysTunnelEventsDict.Names.TEST_TOPIC)
public class RedisSampleSubscriber implements ITunnelSubscriber<RedisSampleData> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(String channel, RedisSampleData msg) {
		LOGGER.info("======onMessage1==={} ====  {}", channel,
				JsonUtil.toJson(msg));
	}

}
