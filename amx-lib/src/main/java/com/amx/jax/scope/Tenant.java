package com.amx.jax.scope;

import java.math.BigDecimal;

public enum Tenant {

	/** Kuwait */
	KWT("kw", "91"),

	/** Oman */
	OMN("om", "84"),

	/** Baharain */
	BRN("bhr", "83"),

	/** India */
	IND("in", "94");

	public static final Tenant DEFAULT = KWT;

	private String id;
	private String code;

	Tenant(String id, String code) {
		this.id = id;
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public BigDecimal getBDCode() {
		return new BigDecimal(code);
	}

}