package com.amx.jax.ui.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.AbstractException;
import com.amx.jax.ui.UIConstants;
import com.bootloaderjs.ContextUtil;

public class ResponseWrapper<T> implements Serializable {

	public ResponseWrapper() {
		super();
		this.timestamp = System.currentTimeMillis();
		this.traceId = ContextUtil.getTraceId();
	}

	public ResponseWrapper(T data) {
		super();
		this.data = data;
		this.timestamp = System.currentTimeMillis();
		this.traceId = ContextUtil.getTraceId();
	}

	private static final long serialVersionUID = 7545829974699803746L;

	private String traceId = null;

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	private Long timestamp = null;
	private String status = "200";
	private WebResponseStatus statusKey = WebResponseStatus.SUCCESS;
	private String message = UIConstants.EMPTY;
	private String messageKey = UIConstants.EMPTY;
	private String redirectUrl = null;

	private List<ResponseError> errors = null;

	public List<ResponseError> getErrors() {
		return errors;
	}

	public void setErrors(List<ResponseError> errors) {
		this.errors = errors;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

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

	public ResponseWrapper<T> updateData(T data) {
		this.setData(data);
		return this;
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

	public WebResponseStatus getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(WebResponseStatus statusKey) {
		this.statusKey = statusKey;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStatus(WebResponseStatus status) {
		this.statusKey = status;
		this.status = status.getCode();
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessage(WebResponseStatus status) {
		this.setStatus(status);
	}

	public void setMessage(WebResponseStatus status, String message) {
		this.setStatus(status);
		this.message = message;
	}

	public void setMessage(WebResponseStatus status, ResponseMessage responseMessage) {
		this.setStatus(status);
		this.messageKey = responseMessage.toString();
		this.message = responseMessage.getMessage();
	}

	public void setMessage(WebResponseStatus status, String messageKey, String message) {
		this.setStatus(status);
		this.messageKey = messageKey;
		this.message = message;
	}

	public void setMessage(WebResponseStatus status, JaxError jaxError, String message) {
		this.setStatus(status);
		this.messageKey = jaxError.toString();
		this.message = message;
	}

	public void setMessage(WebResponseStatus status, AbstractException jaxExcep) {
		this.setMessage(status, jaxExcep.getErrorKey(), jaxExcep.getErrorMessage());
	}

}
