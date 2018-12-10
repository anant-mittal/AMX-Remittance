package com.amx.jax.stomp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;

import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;

@TunnelEventMapping(topic = StompTunnelToAllSender.STOMP_TO_ALL, scheme = TunnelEventXchange.SHOUT_LISTNER,
		integrity = false)
public class StompTunnelToAllSender implements ITunnelSubscriber<StompTunnelEvent> {

	public static final String STOMP_TO_ALL = "STOMP_TO_ALL";

	@Autowired
	private MessageSendingOperations<String> messagingTemplate;

	@Override
	public void onMessage(String channel, StompTunnelEvent msg) {
		messagingTemplate.convertAndSend("/topic" + msg.getTopic(), msg.getData());
	}

}
