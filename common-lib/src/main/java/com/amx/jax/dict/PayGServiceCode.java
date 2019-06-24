package com.amx.jax.dict;

public enum PayGServiceCode {
	KNET("knet"), BENEFIT("benefit"), OMANNET("omannet"), KOMANNET("komannet"), BENEFIT_UPGRADE("benefit_upgrade")

	, WT("wt");

	public static final PayGServiceCode DEFAULT = KNET;

	private String pgEnum;

	PayGServiceCode(String pgEnum) {
		this.pgEnum = pgEnum;
	}

	public String getPgEnum() {
		return this.pgEnum;
	}
}