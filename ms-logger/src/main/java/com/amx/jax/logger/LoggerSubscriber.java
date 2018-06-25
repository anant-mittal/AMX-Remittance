package com.amx.jax.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEvent;
import com.amx.utils.JsonUtil;

@TunnelEvent(topic = AuditService.AUDIT_EVENT_TOPIC)
public class LoggerSubscriber implements ITunnelSubscriber<Object> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(String channel, Object msg) {
		LOGGER.info("======onMessage==={}", JsonUtil.toJson(msg));
	}

}