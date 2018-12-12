package com.amx.jax.tunnel;

import org.redisson.api.RQueue;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.AppContext;
import com.amx.jax.AppContextUtil;
import com.amx.jax.logger.client.AuditServiceClient;
import com.amx.jax.logger.events.RequestTrackEvent;
import com.amx.jax.tunnel.sample.SampleTunnelEventsDict;

@Service
public class TunnelService implements ITunnelService {

	private Logger LOGGER = LoggerFactory.getLogger(TunnelService.class);

	@Autowired(required = false)
	RedissonClient redisson;

	/**
	 * For broadcast purpose, it will send event to all the listeners which are
	 * listening, actively, messages are NOT QUEUED, so there's no guarantee of
	 * messages being deliver if client goes down at the time of shout was
	 * triggered.
	 * 
	 * Though all the listeners can be informed, qualification is done based on
	 * {@link TunnelEventXchange}
	 * 
	 */
	public <T> long shout(String topic, T messagePayload) {
		if (redisson == null) {
			LOGGER.error("No Redissson Client Instance Available");
			return 0L;
		}
		RTopic<TunnelMessage<T>> topicQueue = redisson.getTopic(TunnelEventXchange.SHOUT_LISTNER.getTopic(topic));
		long startTime = System.currentTimeMillis();

		AppContextUtil.setTraceTime(startTime);
		AppContext context = AppContextUtil.getContext();

		TunnelMessage<T> message = new TunnelMessage<T>(messagePayload, context);
		message.setTopic(topic);

		AuditServiceClient.trackStatic(new RequestTrackEvent(RequestTrackEvent.Type.PUB_OUT, message));
		return topicQueue.publish(message);
	}

	/**
	 * For broadcast purpose, it will send event to all the listeners which are
	 * listening, actively, messages are QUEUED, so there's guarantee of messages
	 * being deliver if client goes down at the time of SEND was triggered.
	 * 
	 * Though all the listeners can be informed, qualification is done based on
	 * {@link TunnelEventXchange}
	 * 
	 */
	public <T> long send(String topic, T messagePayload) {
		if (redisson == null) {
			LOGGER.error("No Redissson Client Instance Available");
			return 0L;
		}
		AppContext context = AppContextUtil.getContext();
		TunnelMessage<T> message = new TunnelMessage<T>(messagePayload, context);
		message.setTopic(topic);

		RQueue<TunnelMessage<T>> queue = redisson.getQueue(TunnelEventXchange.SEND_LISTNER.getQueue(topic));
		RTopic<TunnelMessage<T>> topicQueue = redisson.getTopic(TunnelEventXchange.SEND_LISTNER.getTopic(topic));

		AuditServiceClient.trackStatic(new RequestTrackEvent(RequestTrackEvent.Type.PUB_OUT, message));
		queue.add(message);
		return topicQueue.publish(message);
	}

	/**
	 * To assign a job to one of the worker
	 * 
	 * @param topic          - name of task
	 * @param messagePayload - data to be used for task
	 * @return
	 */
	public <T> long task(String topic, T messagePayload) {
		if (redisson == null) {
			return 0L;
		}
		AppContext context = AppContextUtil.getContext();
		TunnelMessage<T> message = new TunnelMessage<T>(messagePayload, context);
		message.setTopic(topic);

		RQueue<TunnelMessage<T>> queue = redisson.getQueue(TunnelEventXchange.TASK_WORKER.getQueue(topic));
		RTopic<String> topicQueue = redisson.getTopic(TunnelEventXchange.TASK_WORKER.getTopic(topic));

		AuditServiceClient.trackStatic(new RequestTrackEvent(RequestTrackEvent.Type.PUB_OUT, message));
		queue.add(message);
		return topicQueue.publish(message.getId());
	}

	/**
	 * To assign a job to one of the worker, based on TaskEvent class, no need to
	 * pass task name, each TaskEvent class is treated as unique task
	 * 
	 * @param event - task event it has be unique
	 * @return
	 */
	public <E extends ITunnelEvent> long task(E event) {
		return this.task(event.getClass().getName(), event);
	}

	/**
	 * For audit purpose only, this will make sure, your log gets delivered to only
	 * one client qualified with {@link TunnelEventMapping#scheme()} as
	 * {@link TunnelEventXchange#AUDIT}
	 */
	public <T> long audit(String topic, T messagePayload) {
		if (redisson == null) {
			return 0L;
		}
		AppContext context = AppContextUtil.getContext();
		TunnelMessage<T> message = new TunnelMessage<T>(messagePayload, context);
		message.setTopic(topic);

		RQueue<TunnelMessage<T>> queue = redisson.getQueue(TunnelEventXchange.AUDIT.getQueue(topic));
		queue.add(message);

		RTopic<String> topicQueue = redisson.getTopic(TunnelEventXchange.AUDIT.getTopic(topic));
		return topicQueue.publish(message.getId());
	}

	public void sayHello() {
		this.shout(SampleTunnelEventsDict.Names.TEST_TOPIC, "Hey There");
	}

}