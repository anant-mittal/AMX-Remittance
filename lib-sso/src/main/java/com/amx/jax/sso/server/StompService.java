package com.amx.jax.sso.server;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.jax.sso.server.SSOStomp.Greeting;

@Component
public class StompService {

	@Async
	@SendTo("/topic/greetings")
	public Greeting repgreeting() throws Exception {
		Thread.sleep(1000); // simulated delay
		return new Greeting("Hello2, RegRettting!");
	}
}
