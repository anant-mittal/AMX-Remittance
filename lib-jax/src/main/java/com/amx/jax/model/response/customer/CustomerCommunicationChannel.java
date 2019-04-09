package com.amx.jax.model.response.customer;

import com.amx.jax.constants.CommunicationChannel;

public class CustomerCommunicationChannel {

	CommunicationChannel channel;
	String maskedValue;

	public CustomerCommunicationChannel(CommunicationChannel channel, String maskedValue) {
		super();
		this.channel = channel;
		this.maskedValue = maskedValue;
	}

	public CustomerCommunicationChannel() {
		super();
	}

	public CommunicationChannel getChannel() {
		return channel;
	}

	public void setChannel(CommunicationChannel channel) {
		this.channel = channel;
	}

	public String getMaskedValue() {
		return maskedValue;
	}

	public void setMaskedValue(String maskedValue) {
		this.maskedValue = maskedValue;
	}

}
