package com.amx.jax.stomp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;

@TunnelEventMapping(scheme = TunnelEventXchange.SHOUT_LISTNER, integrity = false)
public class StompTunnelToUserSender implements ITunnelSubscriber<StompTunnelEvent> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private static final String STOMP_TO_USER = StompTunnelSessionManager.getSystemPrefix() + "_STOMP_TO_USER";

	@Override
	public String getTopic() {
		return STOMP_TO_USER;
	}

	@Override
	public void onMessage(String channel, StompTunnelEvent msg) {
		LOGGER.info("======onMessage1==={} ====  {}", channel, msg);
	}

}
