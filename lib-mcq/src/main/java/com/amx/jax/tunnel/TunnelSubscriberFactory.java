package com.amx.jax.tunnel;

import java.util.List;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TunnelSubscriberFactory {

	private Logger LOGGER = LoggerFactory.getLogger(TunnelSubscriberFactory.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TunnelSubscriberFactory(List<ITunnelSubscriber> listeners,
			@Autowired(required = false) RedissonClient redisson) {

		if (redisson == null) {
			LOGGER.warn("Redisson Not avaiable for {} Listeners", listeners.size());
		} else {
			for (ITunnelSubscriber listener : listeners) {
				Class<?> c = listener.getClass();
				TunnelEvent tunnelEvent = c.getAnnotation(TunnelEvent.class);
				String eventTopic = tunnelEvent.topic();
				this.addListener(eventTopic, redisson, listener);
			}
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
