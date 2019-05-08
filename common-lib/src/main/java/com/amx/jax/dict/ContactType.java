package com.amx.jax.dict;

public enum ContactType {
	SMS, EMAIL, WHATSAPP, SMS_EMAIL,

	// Deprecated
	MOBILE(SMS);

	ContactType contactType;

	ContactType() {
		this.contactType = this;
	}

	ContactType(ContactType contactType) {
		this.contactType = contactType;
	}

	@Override
	public String toString() {
		return this.contactType.name();
	}

	public ContactType contactType() {
		return this.contactType;
	}

}
