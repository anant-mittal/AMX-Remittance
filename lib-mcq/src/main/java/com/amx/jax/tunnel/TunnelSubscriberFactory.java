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
				boolean integrity = tunnelEvent.integrity();
				this.addListener(eventTopic, redisson, listener, integrity);
			}
		}

	}

	public static class WrapperML<M> implements MessageListener<TunnelMessage<M>> {

		ITunnelSubscriber<M> subscriber = null;
		boolean integrity = false;

		public WrapperML(ITunnelSubscriber<M> subscriber, boolean integrity) {
			super();
			this.subscriber = subscriber;
			this.integrity = integrity;
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

	public <M> void addListener(String topic, RedissonClient redisson, ITunnelSubscriber<M> listener,
			boolean integrity) {
		RTopic<TunnelMessage<M>> topicQueue = redisson.getTopic(topic);
		topicQueue.addListener(new WrapperML<M>(listener, integrity) {
			@Override
			public void onMessage(String channel, TunnelMessage<M> msg) {
				RMap<String, String> map = redisson.getMap(channel);
				if (this.integrity) {
					String integrityKey = appConfig.getAppClass() + "#" + listener.getClass().getName() + "#"
							+ msg.getId();

					String prevObject = map.put(integrityKey, msg.getId());
					if (prevObject == null || this.integrity == false) { // Hey I got it first :) OR it doesn't matter
																			// :(
						LOGGER.info("Previous Value was null");
						this.subscriber.onMessage(channel, msg.getData());
						if (this.integrity == false) {
							map.put(integrityKey, "DONE");
						}
					} else { // Hopefully other guy (The Lucky Bugger) is doing his job right.
						LOGGER.info("Message ignored");
					}
				} else {
					this.subscriber.onMessage(channel, msg.getData());
				}
			}
		});
	}

}
