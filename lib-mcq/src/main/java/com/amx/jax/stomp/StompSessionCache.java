package com.amx.jax.stomp;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;
import com.amx.jax.stomp.StompSessionCache.StompSession;

/**
 * The Class StompActiveUsers.
 */
@Component
public class StompSessionCache extends CacheBox<StompSession> {

	public static class StompSession implements Serializable {

		private static final long serialVersionUID = 2457062425857422747L;

		String httpSessionId;
		String prefix;

		public String getPrefix() {
			return prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}

		public String getHttpSessionId() {
			return httpSessionId;
		}

		public void setHttpSessionId(String httpSessionId) {
			this.httpSessionId = httpSessionId;
		}

	}

	/**
	 * Instantiates a new logged in users.
	 */
	public StompSessionCache() {
		super(StompSession.class.getName());
	}

}
