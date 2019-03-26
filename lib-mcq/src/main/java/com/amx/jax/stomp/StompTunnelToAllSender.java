package com.amx.jax.stomp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.core.MessageSendingOperations;

import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.utils.ArgUtil;

@TunnelEventMapping(topic = StompTunnelToAllSender.STOMP_TO_ALL, scheme = TunnelEventXchange.SHOUT_LISTNER,
		integrity = false)
@ConditionalOnProperty("app.stomp")
public class StompTunnelToAllSender implements ITunnelSubscriber<StompTunnelEvent> {

	public static final String STOMP_TO_ALL = "STOMP_TO_ALL";

	@Autowired(required = false)
	private MessageSendingOperations<String> messagingTemplate;

	@Override
	public void onMessage(String channel, StompTunnelEvent msg) {
		if (!ArgUtil.isEmpty(messagingTemplate)) {
			messagingTemplate.convertAndSend("/topic" + msg.getTopic(), msg.getData());
		}
	}

}
