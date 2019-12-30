package com.amx.jax.cache.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.cache.test.RedisSampleCacheBox.RedisSampleData;
import com.amx.jax.tunnel.TunnelService;
import com.amx.jax.tunnel.sample.SampleTunnelEventsDict;

@RestController
public class RedisController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisController.class);

	@Autowired
	RedisSampleCacheBox redisSampleCacheBox;

	@Autowired
	TunnelService tunnelService;

	@RequestMapping(value = "/pub/redis/test", method = RequestMethod.PUT)
	public RedisSampleData cacheTestGet(@RequestBody RedisSampleData status) {
		redisSampleCacheBox.fastPut(status);
		return status;
	}

	@RequestMapping(value = "/pub/redis/test", method = RequestMethod.GET)
	public RedisSampleData cacheTestGet() {
		return redisSampleCacheBox.get();
	}

	@RequestMapping(value = "/pub/redis/test", method = RequestMethod.POST)
	public long cacheTestPost(@RequestBody RedisSampleData status) {
		return tunnelService.shout(SampleTunnelEventsDict.Names.TEST_TOPIC, status);
	}
}
