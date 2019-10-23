package com.amx.jax.radar.logger;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.amx.jax.logger.AuditService;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;

/**
 * The Class LoggerSubscriber.
 */
@ConditionalOnProperty("jax.jobs.audit")
@TunnelEventMapping(topic = AuditService.AUDIT_EVENT_TOPIC, scheme = TunnelEventXchange.AUDIT)
public class LoggerSubscriber implements ITunnelSubscriber<Map<String, Object>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerSubscriber.class);

	@Autowired
	MongoTemplate mongoTemplate;

	@Value("${jax.jobs.audit}")
	private boolean jobAudit;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.tunnel.ITunnelSubscriber#onMessage(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void onMessage(String channel, Map<String, Object> event) {
		LOGGER.debug("onMessage {}", channel);
		if (jobAudit) {
			mongoTemplate.save(event, "AuditEvent");
		}
		// LoggerMapModel eventModel = new LoggerMapModel(event);
	}

}
