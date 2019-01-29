package com.amx.jax.radar.jobs.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;

@TunnelEventMapping(byEvent = SampleTaskEvent.class, scheme = TunnelEventXchange.SHOUT_LISTNER)
public class SampleTaskWorker implements ITunnelSubscriber<SampleTaskEvent> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(String channel, SampleTaskEvent task) {
		LOGGER.info("DONE T:{} by C:{} M:{}", task.getQueue(), task.getCandidate(),task.getMessage());
	}
}