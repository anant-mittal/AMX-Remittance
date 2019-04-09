package com.amx.jax.model.response.customer;

import java.util.List;

public class CustomerModelSignupResponse {

	CustomerFlags customerFlags;
	List<CustomerCommunicationChannel> customerCommunicationChannel;

	public CustomerModelSignupResponse() {
		super();
	}

	public CustomerFlags getCustomerFlags() {
		return customerFlags;
	}

	public void setCustomerFlags(CustomerFlags customerFlags) {
		this.customerFlags = customerFlags;
	}

	public List<CustomerCommunicationChannel> getCustomerCommunicationChannel() {
		return customerCommunicationChannel;
	}

	public void setCustomerCommunicationChannel(List<CustomerCommunicationChannel> customerCommunicationChannel) {
		this.customerCommunicationChannel = customerCommunicationChannel;
	}

}
