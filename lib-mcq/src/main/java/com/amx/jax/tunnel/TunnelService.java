package com.amx.jax.tunnel;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		LOGGER.info("TOPICS===|{}", topicQueue.getChannelNames().toString());
		return topicQueue.publish(new TunnelMessage<T>(messagePayload));
	}

	public void sayHello() {
		this.send(SampleTunnelEvents.Names.TEST_TOPIC, "Hey There");
	}

}