package com.amx.amxlib.model.response;

public enum ResponseStatus {

	OK("200"), NOT_FOUND("404"), INTERNAL_ERROR("500"), BAD_REQUEST("400");

	private final String code;

	public String getCode() {
		return code;
	}

	ResponseStatus(String code) {
		this.code = code;
	}
}
