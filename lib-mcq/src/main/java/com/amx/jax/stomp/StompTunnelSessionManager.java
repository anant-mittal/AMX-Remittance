package com.amx.jax.stomp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.stomp.StompSessionCache.StompSession;
import com.amx.utils.ArgUtil;
import com.amx.utils.UniqueID;

@Component
public class StompTunnelSessionManager {

	/*
	 * Map for <httpSessionId, stompUID>
	 */
	public Map<String, String> http2stompUIdMap = Collections.synchronizedMap(new HashMap<String, String>());

	/*
	 * Map for <wsSessionID, httpSessionId>
	 */
	public Map<String, String> ws2httpMap = Collections.synchronizedMap(new HashMap<String, String>());

	/*
	 * Map for <stompUID, stompSession>
	 */
	@Autowired
	StompSessionCache stompSessionCache;

	public static String getSystemPrefix() {
		return UniqueID.PREF;
	}

	public String createSessionMapping(String wsSessionID, String httpSessionId, String stompUID) {
		if (ArgUtil.isEmpty(stompUID)) {
			stompUID = http2stompUIdMap.get(httpSessionId);
			if (ArgUtil.isEmpty(stompUID)) {
				stompUID = String.format("%s-%s-%s", getSystemPrefix(), httpSessionId, wsSessionID);
				http2stompUIdMap.put(httpSessionId, stompUID);
			}
		}
		ws2httpMap.put(wsSessionID, httpSessionId);
		return stompUID;
	}

	/**
	 * Returns SessionUID for httpSessionId
	 * 
	 * @param httpSessionId
	 * @return
	 */
	public String getSessionUId(String httpSessionId) {
		return http2stompUIdMap.get(httpSessionId);
	}

	public void delinkWs2Http(String httpSessionId, String wsSessionID) {
		ws2httpMap.remove(wsSessionID);
		boolean isExists = false;
		for (Entry<String, String> entry : ws2httpMap.entrySet()) {
			if (entry.getValue().equals(httpSessionId)) {
				isExists = true;
			}
		}
		if (!isExists) {
			http2stompUIdMap.remove(httpSessionId);
		}
	}

	/**
	 * 
	 * @param stompUID      - only one session with one stompUID can exists, if you
	 *                      want to support multiple, change accordingly
	 * @param httpSessionId
	 */
	public void mapHTTPSession(String stompUID, String httpSessionId) {
		StompSession stompSession = new StompSession();
		stompSession.setPrefix(getSystemPrefix());
		stompSession.setHttpSessionId(httpSessionId);
		stompSessionCache.put(stompUID, stompSession);
	}

	public StompSession getStompSession(String stompUID) {
		return stompSessionCache.get(stompUID);
	}
}
