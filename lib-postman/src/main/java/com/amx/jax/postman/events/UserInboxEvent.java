package com.amx.jax.postman.events;

import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.tunnel.ITunnelEvent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInboxEvent implements ITunnelEvent {

	private static final long serialVersionUID = -4488174520614920589L;

	private String to;
	private String from;
	private String message;
	private WAMessage.Channel waChannel;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public WAMessage.Channel getWaChannel() {
		return waChannel;
	}

	public void setWaChannel(WAMessage.Channel waChannel) {
		this.waChannel = waChannel;
	}

}
