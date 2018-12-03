package com.amx.jax.sso.server;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SSOStomp {

	public static class Greeting implements Serializable {
		private static final long serialVersionUID = 1455273263089237654L;
		String name;

		public Greeting() {
		}

		public Greeting(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	@Autowired
	StompService stompService;

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(Greeting message) throws Exception {
		Thread.sleep(1000); // simulated delay
		stompService.repgreeting();
		return new Greeting("Hello, " + message.getName() + "!");
	}

}
