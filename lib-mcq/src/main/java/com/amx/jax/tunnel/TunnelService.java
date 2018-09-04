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

@Service
public class TunnelService implements ITunnelService {

	private Logger LOGGER = LoggerFactory.getLogger(TunnelService.class);

	@Autowired(required = false)
	RedissonClient redisson;

	public <T> long send(String topic, T messagePayload) {
		if (redisson == null) {
			LOGGER.error("No Redissson Client Instance Available");
			return 0L;
		}
		RTopic<TunnelMessage<T>> topicQueue = redisson.getTopic(topic);
		long startTime = System.currentTimeMillis();

		AppContextUtil.setTraceTime(startTime);
		AppContext context = AppContextUtil.getContext();

		TunnelMessage<T> message = new TunnelMessage<T>(messagePayload, context);
		message.setTopic(topic);

		AuditServiceClient.trackStatic(new RequestTrackEvent(RequestTrackEvent.Type.PUB_OUT, message));
		return topicQueue.publish(message);
	}

	public <T> long audit(String topic, T messagePayload) {
		if (redisson == null) {
			return 0L;
		}

		AppContext context = AppContextUtil.getContext();
		TunnelMessage<T> message = new TunnelMessage<T>(messagePayload, context);
		message.setTopic(topic);

		RQueue<TunnelMessage<T>> queue = redisson.getQueue(TunnelEventScheme.AUDIT.getQueue(topic));
		queue.add(message);

		RTopic<String> topicQueue = redisson.getTopic(TunnelEventScheme.AUDIT.getTopic(topic));
		return topicQueue.publish(message.getId());
	}

	public void sayHello() {
		this.send(SampleTunnelEvents.Names.TEST_TOPIC, "Hey There");
	}

}