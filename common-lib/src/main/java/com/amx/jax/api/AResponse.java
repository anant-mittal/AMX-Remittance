package com.amx.jax.api;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.amx.jax.exception.IExceptionEnum;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AResponse<T> {

	protected Long timestamp;

	// Spring Norms
	protected String status = "200"; // 400
	protected String error; // Bad Request
	protected String exception; // org.springframework.http.converter.HttpMessageNotReadableException
	protected String message;// JSON parse error
	protected String path; // postman/email/send
	protected String messageKey;

	/** The status key. */
	protected String statusKey = "SUCCESS";

	// Amx Specs
	protected T meta;
	protected List<AmxFieldError> errors = null;

	public AResponse() {
		this.timestamp = System.currentTimeMillis();
		this.meta = null;
	}

	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public Long getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp.
	 *
	 * @param timestamp
	 *            the new timestamp
	 */
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * HTTP Status Code : 400
	 * 
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * HTTP Status Code : 400
	 * 
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonIgnore
	public void setStatusEnum(IExceptionEnum status) {
		if (!ArgUtil.isEmpty(status)) {
			this.statusKey = status.getStatusKey();
			this.status = ArgUtil.parseAsString(status.getStatusCode());
		}
	}

	/**
	 * Gets the status key.
	 *
	 * @return the status key
	 */
	public String getStatusKey() {
		return statusKey;
	}

	/**
	 * Sets the status key.
	 *
	 * @param statusKey
	 *            the new status key
	 */
	public void setStatusKey(String statusKey) {
		this.statusKey = statusKey;
	}

	/**
	 * Sets the status.
	 *
	 * @param status
	 *            the new status
	 */
	@JsonIgnore
	public void setHttpStatus(HttpStatus status) {
		if (status.is5xxServerError() || status.is4xxClientError() || status.is3xxRedirection()) {
			this.statusKey = status.series().name();
		}
		this.status = ArgUtil.parseAsString(status.value());
		this.message = status.getReasonPhrase();
	}

	/**
	 * Error Type
	 * 
	 * @return
	 */
	public String getError() {
		return error;
	}

	/**
	 * Error Type
	 * 
	 * @return
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Get Exception class of error (Spring Norms)
	 * 
	 * @return
	 */
	public String getException() {
		return exception;
	}

	/**
	 * Set Exception class of error (Spring Norms)
	 * 
	 * @return
	 */
	public void setException(String exception) {
		this.exception = exception;
	}

	/**
	 * Get Exception Message (Spring Norms)
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set Exception Message (Spring Norms)
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * API url
	 * 
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * API url
	 * 
	 * @return
	 */
	public void setPath(String path) {
		this.path = path;
	}

	public T getMeta() {
		return meta;
	}

	public void setMeta(T meta) {
		this.meta = meta;
	}

	/**
	 * Gets the errors.
	 *
	 * @return the errors
	 */
	public List<AmxFieldError> getErrors() {
		return errors;
	}

	/**
	 * Sets the errors.
	 *
	 * @param errors
	 *            the new errors
	 */
	public void setErrors(List<AmxFieldError> errors) {
		this.errors = errors;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

}
