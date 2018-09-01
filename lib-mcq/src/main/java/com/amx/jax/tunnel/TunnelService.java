package com.amx.jax.tunnel;

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
		AuditServiceClient.trackStatic(new RequestTrackEvent(RequestTrackEvent.Type.PUB_OUT, message));
		return topicQueue.publish(message);
	}

	public <T> long nolog(String topic, T messagePayload) {
		if (redisson == null) {
			LOGGER.error("No Redissson Client Instance Available");
			return 0L;
		}
		RTopic<TunnelMessage<T>> topicQueue = redisson.getTopic(topic);
		AppContext context = AppContextUtil.getContext();
		TunnelMessage<T> message = new TunnelMessage<T>(messagePayload, context);
		return topicQueue.publish(message);
	}

	public void sayHello() {
		this.send(SampleTunnelEvents.Names.TEST_TOPIC, "Hey There");
	}

}