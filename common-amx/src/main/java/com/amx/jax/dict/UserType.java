package com.amx.jax.dict;

public enum UserType {

	USER("U"), 
	ADMIN("A"),
	D("D");
	

	private String type;

	UserType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
