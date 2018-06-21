package com.amx.jax.tunnel;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;

public abstract class TunnelSubscriber<T> implements MessageListener<T> {

	public void addListener(String topic, RedissonClient redisson) {
		RTopic<T> topicQueue = redisson.getTopic(topic);
		topicQueue.addListener(this);
	}

}
