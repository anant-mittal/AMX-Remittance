package com.amx.jax.tunnel;

import java.util.List;

import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class TunnelClientFactory {

	@SuppressWarnings("rawtypes")
	public TunnelClientFactory(List<TunnelSubscriber> listeners, RedissonClient redisson) {
		for (TunnelSubscriber listener : listeners) {
			Class<?> c = listener.getClass();
			TunnelEvent tunnelEvent = c.getAnnotation(TunnelEvent.class);
			String eventTopic = tunnelEvent.topic();
			listener.addListener(eventTopic, redisson);
		}
	}

}
