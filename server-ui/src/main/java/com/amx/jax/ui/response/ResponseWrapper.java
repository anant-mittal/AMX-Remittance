package com.amx.jax.ui.response;

import java.io.Serializable;

import com.amx.jax.ui.Constants;
import com.amx.jax.ui.ResponseMessage;
import com.amx.jax.ui.ResponseStatus;

public class ResponseWrapper<T extends ResponseDataInterface> implements Serializable {

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
	private String messageKey = Constants.EMPTY;

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

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

	public void setStatus(ResponseStatus status) {
		this.statusKey = status.getKey();
		this.status = status.getCode();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessage(ResponseStatus status, String message) {
		this.setStatus(status);
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setMessage(ResponseMessage responseMessage) {
		this.messageKey = responseMessage.getMessage();
		this.setStatus(responseMessage.getStatus());
	}

	public void setMessage(ResponseMessage responseMessage, String message) {
		this.messageKey = responseMessage.getMessage();
		this.setMessage(responseMessage.getStatus(), message);
	}

}
