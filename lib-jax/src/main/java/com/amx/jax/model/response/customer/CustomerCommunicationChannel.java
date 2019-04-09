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

}
