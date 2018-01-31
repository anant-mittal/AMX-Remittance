package com.amx.jax.tunnel;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

public class TunnelSubscriber {

	@Autowired
	RedissonClient redisson;

	public <T> void subscribe(String topicName) {
		RTopic<T> topicQ = redisson.getTopic(getClass().getName() + "." + topicName);
		topicQ.addListener(new MessageListener<T>() {
			@Override
			public void onMessage(String channel, T msg) {

			}
		});
	}

}
