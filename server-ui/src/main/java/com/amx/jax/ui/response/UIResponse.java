package com.amx.jax.ui.response;

import java.io.Serializable;

import com.amx.jax.ui.Constants;
import com.amx.jax.ui.EnumUtil.StatusCode;

public class UIResponse<T extends ResponseData> implements Serializable {

	private static final long serialVersionUID = 7545829974699803746L;

	private Long timestamp = null;
	private String status = "200";
	private String statusKey = null;
	private String message = Constants.EMPTY;
	private String error = Constants.EMPTY;

	private T data = null;

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(StatusCode status) {
		this.statusKey = status.getKey();
		this.status = status.getCode();
	}

	public void setStatusKey(String statusKey) {
		this.statusKey = statusKey;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getStatusCode() {
		return status;
	}

	public void setStatusCode(String statusCode) {
		this.status = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
