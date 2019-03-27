package com.amx.jax.tunnel;

public interface ITunnelService {
	public <T> long shout(String topic, T messagePayload);

	public <T> long audit(String topic, T messagePayload);

	public <T> long send(String topic, T messagePayload);

	public <E extends ITunnelEvent> long shout(E event);

	public <E extends ITunnelEvent> long task(E event);

	public <T> long task(String topic, T messagePayload);
}
