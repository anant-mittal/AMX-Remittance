package com.amx.jax.tunnel;

import java.util.List;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TunnelClientFactory {

	@Autowired
	RedissonClient redisson;

	@Autowired
	public TunnelClientFactory(List<TunnelSubscriber> listeners) {
		for (TunnelSubscriber listener : listeners) {
			RTopic<listener.getClass()> topic = redisson.getTopic(listener.getTopic());
			topic.addListener(listener);
		}
	}
}
