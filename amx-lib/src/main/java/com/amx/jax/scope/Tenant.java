package com.amx.jax.scope;

public enum Tenant {

	DEFAULT("default"), KUWAIT("kw"), OMAN("om"), BAHRAIN("bhr"), INDIA("in"),KUWAIT_WITH_CODE("kw","91");

	private String id;
	private String code;

	Tenant(String id) {
		this.id = id;
	}

	Tenant(String id,String code) {
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