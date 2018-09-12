package com.amx.jax.postman.model;

public class SMS extends Message {

	private static final long serialVersionUID = 7854194104138401905L;

	public String toText() {
		return this.getMessage();
	}
}
