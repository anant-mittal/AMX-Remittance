package com.amx.jax.ui.response;

import java.io.Serializable;

import com.amx.jax.ui.Constants;
import com.amx.jax.ui.EnumUtil.StatusCode;

public class ResponseWrapper<T extends ResponseData> implements Serializable {

	public ResponseWrapper(T data) {
		super();
		this.data = data;
		this.timestamp = System.currentTimeMillis();
	}

	private static final long serialVersionUID = 7545829974699803746L;

	private Long timestamp = null;
	private String status = "200";
	private String statusKey = "";
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

	public void setStatusKey(String statusKey) {
		this.statusKey = statusKey;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStatus(StatusCode status) {
		this.statusKey = status.getKey();
		this.status = status.getCode();
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setError(StatusCode status, String error) {
		this.setStatus(status);
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessage(StatusCode status, String message) {
		this.setStatus(status);
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
