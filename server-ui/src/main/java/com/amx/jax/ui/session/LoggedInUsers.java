package com.amx.jax.ui.session;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;

/**
 * The Class LoggedInUsers.
 */
@Component
public class LoggedInUsers extends CacheBox<String> {

	/**
	 * Instantiates a new logged in users.
	 */
	public LoggedInUsers() {
		super("LoggedInUsers");
	}

}
