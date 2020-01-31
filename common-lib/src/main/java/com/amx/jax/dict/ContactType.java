package com.amx.jax.dict;

public enum ContactType {
	SMS("sms", "Mobile No."), EMAIL("email", "Email Id"), WHATSAPP("wa", "WhatsApp No."),
	SMS_EMAIL("esms", "Mobile/Email"),
	FBPUSH("push"),
	NOTP_APP("notpapp"),
	// Deprecated
	@Deprecated
	MOBILE(SMS),
	// Default Null Value
	EMPTY("");

	ContactType contactType;
	String shortCode;
	private String label;

	ContactType(String shortCode) {
		this.contactType = this;
		this.shortCode = shortCode;
		this.label = null;
	}

	ContactType(String shortCode, String label) {
		this.contactType = this;
		this.shortCode = shortCode;
		this.label = label;
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

	public String getLabel() {
		return label;
	}
}
