package com.amx.jax.tunnel;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;

public abstract class TunnelSubscriber<T> implements MessageListener<T> {

	public String getTopic() {
		return TunnelClient.TEST_TOPIC;
	}

	public void addListener(RedissonClient redisson) {
		RTopic<T> topic = redisson.getTopic(this.getTopic());
		topic.addListener(this);
	}

	public TunnelSubscriber(RedissonClient redisson) {
		this.addListener(redisson);
	}

}
