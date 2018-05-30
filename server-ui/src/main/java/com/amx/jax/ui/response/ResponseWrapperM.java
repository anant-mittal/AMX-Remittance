package com.amx.jax.ui.response;

import java.io.Serializable;
import java.util.List;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.AbstractException;
import com.amx.jax.ui.UIConstants;
import com.amx.utils.ContextUtil;

public class ResponseWrapperM<T, M> implements Serializable {

	private static final long serialVersionUID = 7545829974699803746L;

	private Long timestamp = null;
	private String status = "200";
	private WebResponseStatus statusKey = WebResponseStatus.SUCCESS;
	private String message = UIConstants.EMPTY;
	private String messageKey = UIConstants.EMPTY;
	private String redirectUrl = null;

	private T data = null;
	private M meta = null;

	private List<ResponseError> errors = null;

	public ResponseWrapperM() {
		super();
		this.timestamp = System.currentTimeMillis();
		this.traceId = ContextUtil.getTraceId();
	}

	public ResponseWrapperM(T data) {
		this();
		this.data = data;
	}

	public ResponseWrapperM(T data, M meta) {
		this(data);
		this.meta = meta;
	}

	private String traceId = null;

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public ResponseWrapperM<T, M> updateData(T data) {
		this.setData(data);
		return this;
	}

	public ResponseWrapperM<T, M> updateMeta(M meta) {
		this.setMeta(meta);
		return this;
	}

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

	public M getMeta() {
		return meta;
	}

	public void setMeta(M meta) {
		this.meta = meta;
	}

}
