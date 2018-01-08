package com.amx.jax.scope;

public enum Tenant {

	DEFAULT("default", "0"),
	/** Kuwait */
	KWT("kw", "91"),

	/** Oman */
	OMN("om", "84"),

	/** Baharain */
	BRN("bhr", "83"),

	/** India */
	IND("in", "94");

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
}