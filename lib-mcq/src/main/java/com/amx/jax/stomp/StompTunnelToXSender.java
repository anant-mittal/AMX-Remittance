package com.amx.jax.stomp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.core.MessageSendingOperations;

import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.utils.ArgUtil;

@TunnelEventMapping(scheme = TunnelEventXchange.SHOUT_LISTNER, integrity = false)
@ConditionalOnProperty("app.stomp")
public class StompTunnelToXSender implements ITunnelSubscriber<StompTunnelEvent> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public static String getSendTopic(String prefix) {
		return prefix + "_STOMP_TO";
	}

	@Autowired
	StompTunnelSessionManager stompTunnelSessionManager;

	@Autowired(required = false)
	private MessageSendingOperations<String> messagingTemplate;

	@Override
	public String getTopic() {
		return getSendTopic(StompTunnelSessionManager.getSystemPrefix());
	}

	@Override
	public void onMessage(String channel, StompTunnelEvent msg) {
		if (!ArgUtil.isEmpty(msg.getHttpSessionId())) {
			String sessionUId = stompTunnelSessionManager.getSessionUId(msg.getHttpSessionId());
			if (!ArgUtil.isEmpty(sessionUId)) {
				messagingTemplate.convertAndSend("/queue/" + sessionUId + msg.getTopic(), msg.getData());
			}
		}
	}

}
