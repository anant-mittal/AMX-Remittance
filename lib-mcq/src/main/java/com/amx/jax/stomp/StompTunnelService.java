package com.amx.jax.stomp;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.stomp.StompSessionCache.StompSession;
import com.amx.jax.tunnel.TunnelService;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@Component
public class StompTunnelService {

	public static Logger LOGGER = LoggerService.getLogger(StompTunnelService.class);

	@Autowired
	TunnelService tunnelService;

	@Autowired
	StompTunnelSessionManager stompTunnelSessionManager;

	@Async
	public void sendToAll(String topic, Object message) {
		try {
			StompTunnelEvent event = new StompTunnelEvent();
			event.setTopic(topic);
			Map<String, Object> messageData = new HashMap<String, Object>();
			messageData.put("data", message);
			event.setData(JsonUtil.toJsonMap(messageData));
			tunnelService.shout(StompTunnelToAllSender.STOMP_TO_ALL, event);
		} catch (Exception e) {
			LOGGER.error("Error While Sending StompMessage", e);
		}

	}

	/**
	 * This method will work only if
	 * {@link StompTunnelSessionManager#mapHTTPSession(stompUID, String)} has been
	 * called already for the session
	 * 
	 * @param stompUID - UNIQUE ID to Identify End User
	 * @param topic
	 * @param message
	 */
	@Async
	public void sendTo(String stompUID, String topic, Object message) {
		try {
			StompTunnelEvent event = new StompTunnelEvent();
			event.setTopic(topic);
			StompSession stompSession = stompTunnelSessionManager.getStompSession(stompUID);
			if (!ArgUtil.isEmpty(stompSession)) {
				event.setHttpSessionId(stompSession.getHttpSessionId());
				Map<String, Object> messageData = new HashMap<String, Object>();
				messageData.put("data", message);
				event.setData(JsonUtil.toJsonMap(messageData));
				tunnelService.shout(StompTunnelToXSender.getSendTopic(stompSession.getPrefix()), event);
			} else {
				LOGGER.error("stompSession for stompUID {}  not found to send on topic {}", stompUID, topic);
			}
		} catch (Exception e) {
			LOGGER.error("Error While Sending StompMessage to stompUID " + stompUID, e);
		}
	}
}
