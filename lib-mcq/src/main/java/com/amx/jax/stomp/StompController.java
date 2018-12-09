package com.amx.jax.stomp;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.amx.jax.AppConstants;
import com.amx.utils.ArgUtil;

@Controller
public class StompController {

	@Autowired
	StompTunnelSessionManager stompTunnelSessionManager;

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
}
