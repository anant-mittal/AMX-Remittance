package com.amx.jax.tunnel;

public interface ITunnelService {
	public <T> long send(String topic, T messagePayload);
}
