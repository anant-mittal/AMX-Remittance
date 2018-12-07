package com.amx.jax.stomp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
public class StompTunnelService {

	@Autowired
	private MessageSendingOperations<String> messagingTemplate;

	public void send(String topic, Object message) {
		messagingTemplate.convertAndSend("/topic" + topic, message);
	}

	public void senduser(String topic, Object message) {
		messagingTemplate.convertAndSend("/topic" + topic, message);
	}
}
