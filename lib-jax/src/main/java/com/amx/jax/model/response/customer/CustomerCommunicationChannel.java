package com.amx.jax.model.response.customer;

import com.amx.jax.dict.ContactType;

public class CustomerCommunicationChannel {

	ContactType contactType;
	String maskedValue;

	public CustomerCommunicationChannel(ContactType contactType, String maskedValue) {
		super();
		this.contactType = contactType;
		this.maskedValue = maskedValue;
	}

	public CustomerCommunicationChannel() {
		super();
	}

	public ContactType getContactType() {
		return contactType;
	}

	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}

	public String getMaskedValue() {
		return maskedValue;
	}

	public void setMaskedValue(String maskedValue) {
		this.maskedValue = maskedValue;
	}

}
