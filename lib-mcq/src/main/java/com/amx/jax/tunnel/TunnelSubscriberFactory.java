package com.amx.jax.tunnel;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RMapCache;
import org.redisson.api.RQueue;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.AppConfig;
import com.amx.jax.AppContext;
import com.amx.jax.AppContextUtil;
import com.amx.jax.logger.client.AuditServiceClient;
import com.amx.jax.logger.events.RequestTrackEvent;
import com.amx.utils.ArgUtil;
import com.amx.utils.TimeUtils;

@Service
public class TunnelSubscriberFactory {

	private Logger LOGGER = LoggerFactory.getLogger(TunnelSubscriberFactory.class);
	public static long TIME_TO_EXPIRE = 60;
	public static TimeUnit UNIT_OF_TIME = TimeUnit.MINUTES;

	public static final String STATUS_WORKING = "W";
	public static final String STATUS_DONE = "D";
	public static long TIME_TO_EXPIRE_MILLIS = TIME_TO_EXPIRE * 60 * 1000;

	@Autowired
	AppConfig appConfig;

	public static <A extends Annotation> A getAnnotationProxyReady(Class<?> clazz, Class<A> annotationClass) {
		final A annotation = clazz.getAnnotation(annotationClass);
		if (annotation == null && (clazz.isSynthetic())) { // OK, this is probably proxy
			return getAnnotationProxyReady(clazz.getSuperclass(), annotationClass);
		} else {
			return annotation;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TunnelSubscriberFactory(List<ITunnelSubscriber> listeners,
			@Autowired(required = false) RedissonClient redisson) {

		if (redisson == null) {
			LOGGER.warn("Redisson Not avaiable for {} Listeners", listeners.size());
		} else {
			for (ITunnelSubscriber listener : listeners) {
				Class<?> c = AopProxyUtils.ultimateTargetClass(listener);
				TunnelEventMapping tunnelEvent = getAnnotationProxyReady(c, TunnelEventMapping.class);
				String eventTopic = tunnelEvent.topic();

				if (ArgUtil.isEmpty(eventTopic)) {
					eventTopic = tunnelEvent.byEvent().getName();
				}

				if (!ArgUtil.isEmpty(listener.getTopic())) {
					eventTopic = listener.getTopic();
				}

				boolean integrity = tunnelEvent.integrity();
				TunnelEventXchange scheme = tunnelEvent.scheme();
				if (scheme == TunnelEventXchange.TASK_WORKER) {
					this.addTaskWorker(eventTopic, redisson, listener, integrity, c.getClass().getName());
				} else if (scheme == TunnelEventXchange.AUDIT) {
					this.addAuditListener(eventTopic, redisson, listener, integrity, c.getClass().getName());
				} else if (scheme == TunnelEventXchange.SEND_LISTNER) {
					this.addQueuedListener(eventTopic, redisson, listener, integrity, c.getClass().getName());
				} else {
					this.addShoutListener(eventTopic, redisson, listener, integrity, c.getClass().getName());
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

	public <M> void addShoutListener(String topic, RedissonClient redisson, ITunnelSubscriber<M> listener,
			boolean integrity, String listentName) {
		RTopic<TunnelMessage<M>> topicQueue = redisson.getTopic(TunnelEventXchange.SHOUT_LISTNER.getTopic(topic));
		topicQueue.addListener(new WrapperML<M>(listener, integrity) {
			@Override
			public void onMessage(String channel, TunnelMessage<M> msg) {
				AppContext context = msg.getContext();
				AppContextUtil.setContext(context);
				AppContextUtil.init();
				if (this.integrity) {
					RMapCache<String, String> map = redisson
							.getMapCache(TunnelEventXchange.SHOUT_LISTNER.getStatusMap(topic));
					String integrityKey = appConfig.getAppName() + "#" + listentName + "#" + msg.getId();
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
				AuditServiceClient.trackStatic(
						new RequestTrackEvent(RequestTrackEvent.Type.SUB_IN, TunnelEventXchange.SHOUT_LISTNER, msg));
				try {
					this.subscriber.onMessage(channel, msg.getData());
				} catch (Exception e) {
					LOGGER.error("EXCEPTION EVENT " + channel + " : " + msg.getId(), e);
				}
			}
		});
	}

	public <M> void addQueuedListener(String topicName, RedissonClient redisson, ITunnelSubscriber<M> listener,
			boolean integrity, String listentName) {
		RTopic<TunnelMessage<M>> eventTopic = redisson.getTopic(TunnelEventXchange.SEND_LISTNER.getTopic(topicName));
		eventTopic.addListener(new WrapperML<M>(listener, integrity) {
			@Override
			public void onMessage(String channel, TunnelMessage<M> msg) {
				tryMessage(channel, msg);
				RQueue<TunnelMessage<M>> eventAltQueue = redisson
						.getQueue(TunnelEventXchange.SEND_LISTNER.getQueue(topicName));
				TunnelMessage<M> msg2 = eventAltQueue.poll();
				if (msg2 != null && !TimeUtils.isDead(msg2.getTimestamp(), TIME_TO_EXPIRE_MILLIS)) {
					tryMessage(channel, msg2);
				}
			}

			public boolean tryMessage(String channel, TunnelMessage<M> msg) {
				RMapCache<String, String> map = redisson
						.getMapCache(TunnelEventXchange.SEND_LISTNER.getStatusMap(topicName));
				String integrityKey = appConfig.getAppEnv() + "#" + listentName + "#" + msg.getId();
				String prevObject = map.put(integrityKey, STATUS_WORKING, TIME_TO_EXPIRE, UNIT_OF_TIME);
				LOGGER.debug("tryMessage {} {}", integrityKey, prevObject);
				if (prevObject == null) { // Hey I got it first
					this.doMessage(channel, msg);
					map.put(integrityKey, STATUS_DONE, TIME_TO_EXPIRE, UNIT_OF_TIME);
					return true;
				} else { // I hope, other guy (The Lucky Bugger) is doing his job, right.
					LOGGER.debug("IGNORED EVENT : {} : {}", channel, msg.getId());
					return false;
				}
			}

			public void doMessage(String channel, TunnelMessage<M> msg) {
				AppContextUtil.setContext(msg.getContext());
				AppContextUtil.init();
				AuditServiceClient.trackStatic(
						new RequestTrackEvent(RequestTrackEvent.Type.SUB_IN, TunnelEventXchange.SEND_LISTNER, msg));
				try {
					this.subscriber.onMessage(channel, msg.getData());
				} catch (Exception e) {
					LOGGER.error("EXCEPTION EVENT " + channel + " : " + msg.getId(), e);
				}
			}
		});
	}

	public <M> void addTaskWorker(String topic, RedissonClient redisson, ITunnelSubscriber<M> listener,
			boolean integrity, String listentName) {
		RTopic<String> topicQueue = redisson.getTopic(TunnelEventXchange.TASK_WORKER.getTopic(topic));
		topicQueue.addListener(new MessageListener<String>() {
			@Override
			public void onMessage(String channel, String msgId) {
				RQueue<TunnelMessage<M>> topicMessageQueue = redisson
						.getQueue(TunnelEventXchange.TASK_WORKER.getQueue(topic));
				onMessage(channel, topicMessageQueue);
			}

			private void onMessage(String channel, RQueue<TunnelMessage<M>> topicMessageQueue) {
				TunnelMessage<M> msg = topicMessageQueue.poll();
				if (msg == null) {
					return;
				}
				if (!TimeUtils.isDead(msg.getTimestamp(), TIME_TO_EXPIRE_MILLIS)) {
					AppContext context = msg.getContext();
					AppContextUtil.setContext(context);
					AppContextUtil.init();
					AuditServiceClient.trackStatic(
							new RequestTrackEvent(RequestTrackEvent.Type.SUB_IN, TunnelEventXchange.TASK_WORKER, msg));
					try {
						listener.onMessage(channel, msg.getData());
					} catch (Exception e) {
						LOGGER.error("EXCEPTION EVENT " + channel + " : " + msg.getId(), e);
					}
				}
				onMessage(channel, topicMessageQueue);
			}

		});
	}

	public <M> void addAuditListener(String topic, RedissonClient redisson, ITunnelSubscriber<M> listener,
			boolean integrity, String listentName) {
		RTopic<String> topicQueue = redisson.getTopic(TunnelEventXchange.AUDIT.getTopic(topic));
		topicQueue.addListener(new MessageListener<String>() {
			@Override
			public void onMessage(String channel, String msgId) {
				RQueue<TunnelMessage<M>> topicMessageQueue = redisson
						.getQueue(TunnelEventXchange.AUDIT.getQueue(topic));
				onMessage(channel, topicMessageQueue);
			}

			private void onMessage(String channel, RQueue<TunnelMessage<M>> topicMessageQueue) {
				TunnelMessage<M> msg = topicMessageQueue.poll();
				if (msg != null) {
					AppContext context = msg.getContext();
					AppContextUtil.setContext(context);
					AppContextUtil.init();
					try {
						listener.onMessage(channel, msg.getData());
					} catch (Exception e) {
						LOGGER.error("EXCEPTION EVENT " + channel + " : " + msg.getId(), e);
					}
					onMessage(channel, topicMessageQueue);
				}
			}
		});
	}

}
