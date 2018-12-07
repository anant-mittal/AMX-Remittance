package com.amx.jax.stomp;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.amx.utils.ArgUtil;
import com.amx.utils.UniqueID;

@Component
public class StompTunnelSessionManager {

	public Map<String, String> sessionMap = new HashMap<String, String>();
	public Map<String, String> wsessionMap = new HashMap<String, String>();

	public String getSystemPrefix() {
		return UniqueID.PREF;
	}

	public String createSessionMapping(String wsSessionID, String sessionID, String sessionUID) {
		if (ArgUtil.isEmpty(sessionUID)) {
			sessionUID = sessionMap.get(sessionID);
			if (ArgUtil.isEmpty(sessionUID)) {
				sessionUID = String.format("%s-%s-%s", getSystemPrefix(), sessionID, wsSessionID);
				sessionMap.put(sessionID, sessionUID);
			}
		}
		wsessionMap.put(wsSessionID, sessionID);
		return sessionUID;
	}

	public String getSessionUId(String httpSessionId) {
		return sessionMap.get(httpSessionId);
	}

	public void removeSessionUId(String sessionId, String wsSessionID) {
		wsessionMap.remove(wsSessionID);
		boolean isExists = false;
		for (Entry<String, String> entry : wsessionMap.entrySet()) {
			if (entry.getValue().equals(sessionId)) {
				isExists = true;
			}
		}
		if (!isExists) {
			sessionMap.remove(sessionId);
		}
	}
}
