package com.amx.jax.tunnel;

public interface ITunnelService {
	public <T> long send(String topic, T messagePayload);

	public <T> long nolog(String topic, T messagePayload);
}
