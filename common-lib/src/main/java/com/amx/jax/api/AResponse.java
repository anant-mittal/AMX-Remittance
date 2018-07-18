package com.amx.jax.api;

import java.util.List;

public abstract class AResponse<T> {

	protected Long timestamp;

	// Spring Norms
	protected String status = "200"; // 400
	protected String error; // Bad Request
	protected String exception; // org.springframework.http.converter.HttpMessageNotReadableException
	protected String message;// JSON parse error
	protected String path; // postman/email/send

	// Amx Specs
	protected T meta;
	private List<AmxFieldError> errors = null;

	public AResponse() {
		this.timestamp = System.currentTimeMillis();
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
}
