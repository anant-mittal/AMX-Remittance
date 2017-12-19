package com.amx.jax.scope;

public enum Tenant {

	DEFAULT("default"), KUWAIT("kw"), OMAN("om"), BAHRAIN("bhr"), INDIA("in");

	private String id;

	Tenant(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}