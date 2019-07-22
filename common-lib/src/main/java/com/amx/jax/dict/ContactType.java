package com.amx.jax.dict;

public enum ContactType {
	SMS("sms"), EMAIL("email"), WHATSAPP("wa"), SMS_EMAIL("esms"),

	// Deprecated
	MOBILE(SMS);

	ContactType contactType;
	String shortCode;

	ContactType(String shortCode) {
		this.contactType = this;
		this.shortCode = shortCode;
	}

	ContactType(ContactType contactType) {
		this.contactType = contactType;
		this.shortCode = contactType.getShortCode();
	}

	@Override
	public String toString() {
		return this.contactType.name();
	}

	public ContactType contactType() {
		return this.contactType;
	}

	public String getShortCode() {
		return shortCode;
	}

}
