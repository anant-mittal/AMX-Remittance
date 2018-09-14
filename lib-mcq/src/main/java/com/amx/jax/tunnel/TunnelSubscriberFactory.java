package com.amx.jax.tunnel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RMapCache;
import org.redisson.api.RQueue;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.AppConfig;
import com.amx.jax.AppContext;
import com.amx.jax.AppContextUtil;
import com.amx.jax.logger.client.AuditServiceClient;
import com.amx.jax.logger.events.RequestTrackEvent;

@Service
public class TunnelSubscriberFactory {

	private Logger LOGGER = LoggerFactory.getLogger(TunnelSubscriberFactory.class);
	public static long TIME_TO_EXPIRE = 10;
	public static TimeUnit UNIT_OF_TIME = TimeUnit.MINUTES;

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
				TunnelEventXchange scheme = tunnelEvent.scheme();
				if (scheme == TunnelEventXchange.AUDIT) {
					this.addAuditListener(eventTopic, redisson, listener, integrity);
				} else {
					this.addListener(eventTopic, redisson, listener, integrity);
				}
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
				AppContext context = msg.getContext();
				AppContextUtil.setContext(context);
				if (this.integrity) {
					RMapCache<String, String> map = redisson.getMapCache(channel);
					String integrityKey = appConfig.getAppEnv() + "#" + appConfig.getAppName() + "#"
							+ listener.getClass().getName() + "#" + msg.getId();
					String prevObject = map.put(integrityKey, msg.getId(), TIME_TO_EXPIRE, UNIT_OF_TIME);
					if (prevObject == null) { // Hey I got it first :) OR it doesn't matter
						this.doMessage(channel, msg);
						map.put(integrityKey, "DONE", TIME_TO_EXPIRE, UNIT_OF_TIME);
					} else { // I hope, other guy (The Lucky Bugger) is doing his job, right.
						LOGGER.debug("IGNORED EVENT : {} : {}", channel, msg.getId());
					}
				} else {
					this.doMessage(channel, msg);
				}
			}

			public void doMessage(String channel, TunnelMessage<M> msg) {
				AuditServiceClient.trackStatic(new RequestTrackEvent(RequestTrackEvent.Type.SUB_IN, msg));
				try {
					this.subscriber.onMessage(channel, msg.getData());
				} catch (Exception e) {
					LOGGER.error("EXCEPTION EVENT " + channel + " : " + msg.getId(), e);
				}
			}
		});
	}

	public <M> void addQueuedListener(String topicName, RedissonClient redisson, ITunnelSubscriber<M> listener,
			boolean integrity) {
		RTopic<TunnelMessage<M>> eventTopic = redisson.getTopic(TunnelEventXchange.SEND_LISTNER.getTopic(topicName));
		eventTopic.addListener(new WrapperML<M>(listener, integrity) {
			@Override
			public void onMessage(String channel, TunnelMessage<M> msg) {
				if (!tryMessage(channel, msg)) {
					RQueue<TunnelMessage<M>> eventAltQueue = redisson
							.getQueue(TunnelEventXchange.SEND_LISTNER.getQueue(topicName));
					TunnelMessage<M> msg2 = eventAltQueue.poll();
					if (msg2 != null) {
						tryMessage(channel, msg2);
					}
				}
			}

			public boolean tryMessage(String channel, TunnelMessage<M> msg) {
				RMapCache<String, String> map = redisson
						.getMapCache(TunnelEventXchange.SEND_LISTNER.getStatusMap(topicName));
				String integrityKey = appConfig.getAppEnv() + "#" + listener.getClass().getName() + "#" + msg.getId();
				String prevObject = map.put(integrityKey, msg.getId(), TIME_TO_EXPIRE, UNIT_OF_TIME);
				if (prevObject == null) { // Hey I got it first
					this.doMessage(channel, msg);
					map.put(integrityKey, appConfig.getAppGroup(), TIME_TO_EXPIRE, UNIT_OF_TIME);
					return true;
				} else { // I hope, other guy (The Lucky Bugger) is doing his job, right.
					LOGGER.debug("IGNORED EVENT : {} : {}", channel, msg.getId());
					return false;
				}
			}

			public void doMessage(String channel, TunnelMessage<M> msg) {
				AppContextUtil.setContext(msg.getContext());
				AuditServiceClient.trackStatic(new RequestTrackEvent(RequestTrackEvent.Type.SUB_IN, msg));
				try {
					this.subscriber.onMessage(channel, msg.getData());
				} catch (Exception e) {
					LOGGER.error("EXCEPTION EVENT " + channel + " : " + msg.getId(), e);
				}
			}
		});
	}

	public <M> void addAuditListener(String topic, RedissonClient redisson, ITunnelSubscriber<M> listener,
			boolean integrity) {
		RTopic<String> topicQueue = redisson.getTopic(TunnelEventXchange.AUDIT.getTopic(topic));
		topicQueue.addListener(new MessageListener<String>() {
			@Override
			public void onMessage(String channel, String msgId) {
				RQueue<TunnelMessage<M>> topicMessageQueue = redisson.getQueue(TunnelEventXchange.AUDIT.getQueue(topic));
				TunnelMessage<M> msg = topicMessageQueue.poll();
				if (msg != null) {
					AppContext context = msg.getContext();
					AppContextUtil.setContext(context);
					try {
						listener.onMessage(channel, msg.getData());
					} catch (Exception e) {
						LOGGER.error("EXCEPTION EVENT " + channel + " : " + msg.getId(), e);
					}
				}
			}
		});
	}

}
