package com.amx.jax.stomp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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

	@Async
	public void sendToAll(String topic, Object message) {
		StompTunnelEvent event = new StompTunnelEvent();
		event.setTopic(topic);
		Map<String, Object> messageData = new HashMap<String, Object>();
		messageData.put("data", message);
		event.setData(JsonUtil.toJsonMap(messageData));
		tunnelService.shout(StompTunnelToAllSender.STOMP_TO_ALL, event);
	}

	@Async
	public void sendTo(String stompSessionId, String topic, Object message) {
		StompTunnelEvent event = new StompTunnelEvent();
		event.setTopic(topic);
		StompSession stompSession = stompTunnelSessionManager.getStompSession(stompSessionId);
		if (!ArgUtil.isEmpty(stompSession)) {
			event.setHttpSessionId(stompSession.getHttpSessionId());
			Map<String, Object> messageData = new HashMap<String, Object>();
			messageData.put("data", message);
			event.setData(JsonUtil.toJsonMap(messageData));
			tunnelService.shout(StompTunnelToXSender.getSendTopic(stompSession.getPrefix()), event);
		}

	}
}
