package com.amx.jax.stomp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Component;

import com.amx.jax.stomp.StompSessionCache.StompSession;
import com.amx.jax.tunnel.TunnelService;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@Component
public class StompTunnelService {

	@Autowired
	TunnelService tunnelService;

	@Autowired
	StompTunnelSessionManager stompTunnelSessionManager;

	public void sendToAll(String topic, Object message) {
		StompTunnelEvent event = new StompTunnelEvent();
		event.setTopic(topic);
		event.setData(JsonUtil.toMap(message));
		tunnelService.shout(StompTunnelToAllSender.STOMP_TO_ALL, event);
	}

	public void sendTo(String stompSessionId, String topic, Object message) {
		StompTunnelEvent event = new StompTunnelEvent();
		event.setTopic(topic);
		StompSession stompSession = stompTunnelSessionManager.getStompSession(stompSessionId);
		if (!ArgUtil.isEmpty(stompSession)) {
			event.setHttpSessionId(stompSession.getHttpSessionId());
			event.setData(JsonUtil.toMap(message));
			tunnelService.shout(StompTunnelToXSender.getSendTopic(stompSession.getPrefix()), event);
		}

	}
}
