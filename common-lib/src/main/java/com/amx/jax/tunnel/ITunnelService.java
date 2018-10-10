package com.amx.jax.tunnel;

public interface ITunnelService {
	public <T> long shout(String topic, T messagePayload);

	public <T> long audit(String topic, T messagePayload);
}
