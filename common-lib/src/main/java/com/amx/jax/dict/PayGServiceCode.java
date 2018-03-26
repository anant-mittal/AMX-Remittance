package com.amx.jax.dict;

public enum PayGServiceCode {
	KNET("knet"), BENEFIT("benefit"), OMANNET("OmanNet");

	public static final PayGServiceCode DEFAULT = KNET;

	private String pgEnum;

	PayGServiceCode(String pgEnum) {
		this.pgEnum = pgEnum;
	}

	public String getPgEnum() {
		return this.pgEnum;
	}
}