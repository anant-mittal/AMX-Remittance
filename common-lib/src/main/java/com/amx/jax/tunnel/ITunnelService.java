package com.amx.jax.tunnel;

public interface ITunnelService {
	public <T> void send(String topic, T messagePayload);
}
