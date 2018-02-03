package com.amx.jax.tunnel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class TunnelPublisherImpl implements TunnelPublisher {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private ChannelTopic topic;

	public void publish(final String message) {
		redisTemplate.convertAndSend("somemessate", message);
	}
}
