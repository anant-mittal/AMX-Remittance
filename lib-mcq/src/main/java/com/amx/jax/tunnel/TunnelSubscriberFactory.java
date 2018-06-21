package com.amx.jax.tunnel;

import java.util.List;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class TunnelSubscriberFactory {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TunnelSubscriberFactory(List<ITunnelSubscriber> listeners, RedissonClient redisson) {

		for (ITunnelSubscriber listener : listeners) {
			Class<?> c = listener.getClass();
			TunnelEvent tunnelEvent = c.getAnnotation(TunnelEvent.class);
			String eventTopic = tunnelEvent.topic();
			this.addListener(eventTopic, redisson, listener);
		}

	}

	public static class WrapperML<M> implements MessageListener<M> {

		ITunnelSubscriber<M> subscriber = null;

		public WrapperML(ITunnelSubscriber<M> subscriber) {
			super();
			this.subscriber = subscriber;
		}

		@Override
		public void onMessage(String channel, M msg) {
			this.subscriber.onMessage(channel, msg);
		}

		public ITunnelSubscriber<M> getSubscriber() {
			return subscriber;
		}

		public void setSubscriber(ITunnelSubscriber<M> subscriber) {
			this.subscriber = subscriber;
		}

	}

	public <M> void addListener(String topic, RedissonClient redisson, ITunnelSubscriber<M> listener) {
		RTopic<M> topicQueue = redisson.getTopic(topic);
		MessageListener<M> lst = new WrapperML<M>(listener);
		topicQueue.addListener(lst);
	}

}
