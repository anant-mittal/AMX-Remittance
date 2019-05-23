package com.amx.jax.cache;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.jax.cache.CacheBox;
import com.amx.jax.session.SessionContextCache;
import com.amx.utils.ArgUtil;

/**
 * The Class LoggedInUsers.
 */
@Component
public class SessionContextCacheImpl extends CacheBox<Map<String, Object>> implements SessionContextCache {

	@Override
	public Map<String, Object> getContext() {
		String sessionid = AppContextUtil.getSessionId(false);
		if (!ArgUtil.isEmpty(sessionid)) {
			return this.getOrDefault(sessionid);
		}
		return null;
	}

	@Override
	public void setContext(Map<String, Object> map) {
		String sessionid = AppContextUtil.getSessionId(false);
		if (!ArgUtil.isEmpty(sessionid)) {
			this.put(sessionid, map);
		}
	}

}
