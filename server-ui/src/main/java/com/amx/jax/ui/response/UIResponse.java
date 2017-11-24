package com.amx.jax.ui.response;

import java.io.Serializable;

import com.amx.jax.ui.Constants;

public class UIResponse implements Serializable {

	private static final long serialVersionUID = 7545829974699803746L;

	private String statusCode = null;

	private String message = Constants.EMPTY;

	private ResponseData data = null;

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ResponseData getData() {
		return data;
	}

	public void setData(ResponseData data) {
		this.data = data;
	}

}
