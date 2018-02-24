package com.amx.jax.tunnel;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class TunnelPublisher {

	private Logger LOGGER = LoggerFactory.getLogger(TunnelPublisher.class);

	@Autowired
	RedissonClient redisson;

	public <T> void send(String topic, T messagePayload) {
		RTopic<T> topicQueue = redisson.getTopic(topic);
		LOGGER.info("======send===");
		topicQueue.publish(messagePayload);
	}
}