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
		if(redisson==null) {
			LOGGER.error("No Redissson Client Instance Available");
			return 0L;
		}
		RTopic<T> topicQueue = redisson.getTopic(topic);
		return topicQueue.publish(messagePayload);
	}

	public void sayHello() {
		this.send(TunnelClient.TEST_TOPIC, "Hey There");
	}

}