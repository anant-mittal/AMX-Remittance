package com.amx.jax.postman.model;

public enum Channel {

	NOTIPY("C9AK11W2K"), DEPLOYER("C8L3GL92A"), GENERAL("C7F823MLJ");

	String code;

	public static final Channel DEFAULT = GENERAL;

	Channel(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
