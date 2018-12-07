package com.amx.jax.stomp;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.JsonPath;

@Component
public class WebSocketSessionListener {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketSessionListener.class.getName());
	private List<String> connectedClientId = new ArrayList<String>();

	public static JsonPath TOKEN_PATH = new JsonPath("/nativeHeaders/token/[0]");
	public static JsonPath ID_PATH = new JsonPath("/nativeHeaders/id/[0]");

	@EventListener
	public void connectionEstablished(SessionConnectedEvent sce) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(sce.getMessage());
		GenericMessage<?> simpConnectMessage = (GenericMessage<?>) sha.getHeader("simpConnectMessage");
		if (!ArgUtil.isEmpty(simpConnectMessage)) {
			String token = TOKEN_PATH.load(simpConnectMessage.getHeaders(), Constants.BLANK);
			String id = ID_PATH.load(simpConnectMessage.getHeaders(), Constants.BLANK);
			logger.info("Connessione websocket Token : {} , Id : {}, SessionId {}", token, id, sha.getSessionId());
		}
	}

	@EventListener
	public void webSockectDisconnect(SessionDisconnectEvent sde) {
//		MessageHeaders msgHeaders = sde.getMessage().getHeaders();
//		Principal princ = (Principal) msgHeaders.get("simpUser");
//		StompHeaderAccessor sha = StompHeaderAccessor.wrap(sde.getMessage());
//		List<String> nativeHeaders = sha.getNativeHeader("token");
//		if (nativeHeaders != null) {
//			String userId = nativeHeaders.get(0);
//			connectedClientId.remove(userId);
//			logger.info("Connessione websocket stabilita. ID Utente " + userId);
//		} else if (!ArgUtil.isEmpty(princ)) {
//			String userId = princ.getName();
//			connectedClientId.remove(userId);
//			logger.info("Connessione websocket stabilita. ID Utente " + userId);
//		}
	}

	public List<String> getConnectedClientId() {
		return connectedClientId;
	}

	public void setConnectedClientId(List<String> connectedClientId) {
		this.connectedClientId = connectedClientId;
	}
}