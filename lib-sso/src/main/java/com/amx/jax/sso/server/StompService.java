package com.amx.jax.sso.server;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.jax.sso.server.SSOStomp.Greeting;

@Component
public class StompService {

	@Async
	public Greeting repgreeting() throws Exception {
		return new Greeting("Hello2, RegRettting!");
	}
}
