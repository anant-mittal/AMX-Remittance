package com.amx.jax.dict;

public interface Communicatable {

	public boolean canSendEmail();

	public boolean canSendWhatsApp();

	public boolean canSendMobile();

	boolean hasVerified(ContactType contactType);

	boolean hasPresent(ContactType contactType);
}
