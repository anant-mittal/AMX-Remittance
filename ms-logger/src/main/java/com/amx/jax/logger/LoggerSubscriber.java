package com.amx.jax.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEvent;

@TunnelEvent(topic = AuditService.AUDIT_EVENT_TOPIC)
public class LoggerSubscriber implements ITunnelSubscriber<Object> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public void onMessage(String channel, Object event) {
		mongoTemplate.save(event, "AuditEvent");
	}

}