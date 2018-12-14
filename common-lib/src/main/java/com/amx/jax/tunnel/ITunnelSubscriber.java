package com.amx.jax.tunnel;

public interface ITunnelSubscriber<M> {

	void onMessage(String channel, M message);

	default String getTopic() {
		return null;
	};

}
