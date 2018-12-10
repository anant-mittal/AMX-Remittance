package com.amx.jax.stomp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.jax.tunnel.TunnelService;
import com.amx.utils.JsonUtil;

@Component
public class StompTunnelService {

	@Autowired
	TunnelService tunnelService;

	@Autowired
	StompTunnelSessionManager stompTunnelSessionManager;

	@Autowired
	private MessageSendingOperations<String> messagingTemplate;

	@Async
	public void sendToAll(String topic, Object message) {
		StompTunnelEvent event = new StompTunnelEvent();
		event.setTopic("/topic" + topic);
		event.setData(JsonUtil.toMap(message));
		tunnelService.shout(StompTunnelToAllSender.STOMP_TO_ALL, event);
	}

	@Async
	public void sendToUser(String userId, String topic, Object message) {
		messagingTemplate.convertAndSend("/topic" + topic, message);
	}
}
