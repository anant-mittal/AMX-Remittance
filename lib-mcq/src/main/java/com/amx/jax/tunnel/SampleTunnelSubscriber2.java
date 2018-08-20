package com.amx.jax.tunnel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.logger.AuditEvent;
import com.amx.utils.JsonUtil;

@TunnelEvent(topic = TunnelClient.TEST_TOPIC)
public class SampleTunnelSubscriber2 implements ITunnelSubscriber<AuditEvent> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(String channel, AuditEvent msg) {
		LOGGER.info("======onMessage2==={} ====  {}",channel, JsonUtil.toJson(msg));
	}

}