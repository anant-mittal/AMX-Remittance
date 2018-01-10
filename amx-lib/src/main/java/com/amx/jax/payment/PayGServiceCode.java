package com.amx.jax.payment;

public enum PayGServiceCode {
	KNET("knet"), BAHKNET("bahknet"), OMANNET("OmanNet"),

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