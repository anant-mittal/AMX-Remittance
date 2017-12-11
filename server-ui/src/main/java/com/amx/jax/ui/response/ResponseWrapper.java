package com.amx.jax.ui.response;

import java.io.Serializable;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.AbstractException;
import com.amx.jax.ui.Constants;

public class ResponseWrapper<T> implements Serializable {

	public ResponseWrapper() {
		super();
		this.timestamp = System.currentTimeMillis();
	}

	public ResponseWrapper(T data) {
		super();
		this.data = data;
		this.timestamp = System.currentTimeMillis();
	}

	private static final long serialVersionUID = 7545829974699803746L;

	private Long timestamp = null;
	private String status = "200";
	private ResponseStatus statusKey = null;
	private String message = Constants.EMPTY;
	private String messageKey = Constants.EMPTY;

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	private T data = null;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	// public void setData(List<T> results) {
	// this.data = results;
	// }

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public ResponseStatus getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(ResponseStatus statusKey) {
		this.statusKey = statusKey;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStatus(ResponseStatus status) {
		this.statusKey = status;
		this.status = status.getCode();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessage(ResponseStatus status) {
		this.setStatus(status);
	}

	public void setMessage(ResponseStatus status, String message) {
		this.setStatus(status);
		this.message = message;
	}

	public void setMessage(ResponseStatus status, ResponseMessage responseMessage) {
		this.setStatus(status);
		this.messageKey = responseMessage.toString();
		this.message = responseMessage.getMessage();
	}

	public void setMessage(ResponseStatus status, String messageKey, String message) {
		this.setStatus(status);
		this.messageKey = messageKey;
		this.message = message;
	}

	public void setMessage(ResponseStatus status, JaxError jaxError, String message) {
		this.setStatus(status);
		this.messageKey = jaxError.toString();
		this.message = message;
	}

	public void setMessage(ResponseStatus status, AbstractException jaxExcep) {
		this.setMessage(status, jaxExcep.getError(), jaxExcep.getErrorMessage());
	}

}
