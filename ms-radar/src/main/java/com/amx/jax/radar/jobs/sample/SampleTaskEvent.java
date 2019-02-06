package com.amx.jax.radar.jobs.sample;

import com.amx.jax.tunnel.TunnelEvent;

public class SampleTaskEvent extends TunnelEvent {

	private static final long serialVersionUID = 7415624585226619390L;

	private String queue;
	private String candidate;
	private String message;

	public SampleTaskEvent(String queue, String candidate, String message) {
		this.queue = queue;
		this.message = message;
		this.candidate = candidate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCandidate() {
		return candidate;
	}

	public void setCandidate(String candidate) {
		this.candidate = candidate;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

}
