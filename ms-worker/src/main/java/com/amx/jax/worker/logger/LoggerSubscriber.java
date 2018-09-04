package com.amx.jax.worker.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.logger.AuditService;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEvent;
import com.amx.jax.tunnel.TunnelEventScheme;

/**
 * The Class LoggerSubscriber.
 */
@TunnelEvent(topic = AuditService.AUDIT_EVENT_TOPIC, scheme = TunnelEventScheme.AUDIT)
public class LoggerSubscriber implements ITunnelSubscriber<Object> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerSubscriber.class);

	// @Autowired
	// MongoTemplate mongoTemplate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.tunnel.ITunnelSubscriber#onMessage(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void onMessage(String channel, Object event) {
		LOGGER.debug("onMessage {}", channel);
		// mongoTemplate.save(event, "AuditEvent");
	}

}