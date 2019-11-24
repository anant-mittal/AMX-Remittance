package com.amx.jax.logger.events;

import java.math.BigDecimal;

import com.amx.jax.dict.ContactType;

public class Verify {

	protected BigDecimal id;
	protected ContactType contactType;
	protected BigDecimal count;

	public Verify() {
		super();
	}

	public BigDecimal getCount() {
		return count;
	}

	public void setCount(BigDecimal count) {
		this.count = count;
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public ContactType getContactType() {
		return contactType;
	}

	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}

	public Verify id(BigDecimal id) {
		this.id = id;
		return this;
	}

	public Verify contactType(ContactType contactType) {
		this.contactType = contactType;
		return this;
	}

	public Verify count(BigDecimal count) {
		this.count = count;
		return this;
	}
}
