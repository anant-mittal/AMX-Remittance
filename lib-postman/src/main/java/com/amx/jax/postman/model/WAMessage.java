package com.amx.jax.postman.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WAMessage extends Message {
	private static final long serialVersionUID = 2765644882757156928L;

	public static enum Channel implements IChannel {
		TWILIO, APIWHA, DEFAULT
	}

	private String id;

	protected Channel channel = Channel.DEFAULT;

	public WAMessage() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
}
