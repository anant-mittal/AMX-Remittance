package com.amx.jax.payment;

public enum PayGServiceCode {
	KNET("knet"), BENEFIT("benefit"), OMANNET("OmanNet"),

	/**
	 * DOnt use this one.
	 */
	DEFAULT("default");

	private String pgEnum;

	PayGServiceCode(String pgEnum) {
		this.pgEnum = pgEnum;
	}

	public String getPgEnum() {
		return this.pgEnum;
	}
}