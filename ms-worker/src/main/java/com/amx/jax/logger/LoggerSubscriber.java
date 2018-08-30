package com.amx.jax.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEvent;

/**
 * The Class LoggerSubscriber.
 */
@TunnelEvent(topic = AuditService.AUDIT_EVENT_TOPIC)
public class LoggerSubscriber implements ITunnelSubscriber<Object> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerSubscriber.class);

	@Autowired
	MongoTemplate mongoTemplate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.tunnel.ITunnelSubscriber#onMessage(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void onMessage(String channel, Object event) {
		LOGGER.debug("onMessage {}", channel);
		mongoTemplate.save(event, "AuditEvent");
	}

}