package com.amx.jax.tunnel;

import java.util.List;

import org.redisson.api.RMap;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.AppConfig;

@Service
public class TunnelSubscriberFactory {

	private Logger LOGGER = LoggerFactory.getLogger(TunnelSubscriberFactory.class);

	@Autowired
	AppConfig appConfig;

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
				boolean queued = tunnelEvent.queued();
				this.addListener(eventTopic, redisson, listener, queued);
			}
		}

	}

	public static class WrapperML<M> implements MessageListener<TunnelMessage<M>> {

		ITunnelSubscriber<M> subscriber = null;
		boolean queued = false;

		public WrapperML(ITunnelSubscriber<M> subscriber, boolean queued) {
			super();
			this.subscriber = subscriber;
			this.queued = queued;
		}

		@Override
		public void onMessage(String channel, TunnelMessage<M> msg) {
			this.subscriber.onMessage(channel, msg.getData());
		}

		public ITunnelSubscriber<M> getSubscriber() {
			return subscriber;
		}

		public void setSubscriber(ITunnelSubscriber<M> subscriber) {
			this.subscriber = subscriber;
		}

	}

	public <M> void addListener(String topic, RedissonClient redisson, ITunnelSubscriber<M> listener, boolean queued) {
		RTopic<TunnelMessage<M>> topicQueue = redisson.getTopic(topic);
		topicQueue.addListener(new WrapperML<M>(listener, queued) {
			@Override
			public void onMessage(String channel, TunnelMessage<M> msg) {
				RMap<String, String> map = redisson.getMap(channel);
				String prevObject = map.put(appConfig.getAppClass() + "#" + msg.getId(), msg.getId());
				if (prevObject == null) {
					LOGGER.info("Previous Value was null");
					this.subscriber.onMessage(channel, msg.getData());
				} else {
					LOGGER.info("Message ignored");
				}

			}
		});
	}

}
