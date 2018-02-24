package com.amx.jax.tunnel;

import org.springframework.stereotype.Service;

@Service
public class TunnelPublisherImpl extends TunnelPublisher {

	public void sayHello() {
		this.send(TunnelClient.TEST_TOPIC, "Hey There");
	}

}
