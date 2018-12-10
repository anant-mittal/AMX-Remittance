package com.amx.jax.stomp;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.amx.jax.AppConstants;
import com.amx.utils.ArgUtil;

@Controller
public class StompController {

	@Autowired
	StompTunnelSessionManager stompTunnelSessionManager;

	@Autowired
	StompTunnelService stompTunnelService;

	@SubscribeMapping("/stomp/tunnel/meta")
	public Map<String, String> meta(SimpMessageHeaderAccessor headerAccessor) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(AppConstants.SESSION_UID_XKEY,
				stompTunnelSessionManager.createSessionMapping(headerAccessor.getSessionId(),
						ArgUtil.parseAsString(headerAccessor.getSessionAttributes().get(AppConstants.SESSION_ID_XKEY)),
						ArgUtil.parseAsString(
								headerAccessor.getSessionAttributes().get(AppConstants.SESSION_UID_XKEY))));
		return map;
	}

	@MessageMapping("/ping")
	public Map<String, String> ping(SimpMessageHeaderAccessor headerAccessor) throws InterruptedException {
		Thread.sleep(1000); // simulated delay
		Map<String, String> map = new HashMap<String, String>();
		map.put("message", "Hey baby ping pong!");
		stompTunnelService.sendToAll("/pong", map);
		return map;
	}
}
