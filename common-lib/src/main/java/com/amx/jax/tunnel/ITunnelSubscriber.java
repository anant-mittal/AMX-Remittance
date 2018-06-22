package com.amx.jax.tunnel;

public interface ITunnelSubscriber<M> {

	void onMessage(String channel, M message);

}
