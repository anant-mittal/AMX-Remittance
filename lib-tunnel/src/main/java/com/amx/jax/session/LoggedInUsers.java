package com.amx.jax.session;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;

@Component
public class LoggedInUsers extends CacheBox<String> {

	public LoggedInUsers() {
		super("LoggedInUsers");
	}

}
