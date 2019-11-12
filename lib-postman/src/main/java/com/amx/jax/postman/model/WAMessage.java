package com.amx.jax.postman.model;

import java.math.BigDecimal;

import com.amx.jax.dict.ContactType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WAMessage extends Message {
	private static final long serialVersionUID = 2765644882757156928L;

	public static enum Channel implements IChannel {
		TWILIO, APIWHA, DEFAULT
	}

	protected Channel channel = Channel.DEFAULT;
	private BigDecimal queue;

	public WAMessage() {
		super(ContactType.WHATSAPP);
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public BigDecimal getQueue() {
		return queue;
	}

	public void setQueue(BigDecimal queue) {
		this.queue = queue;
	}

}
