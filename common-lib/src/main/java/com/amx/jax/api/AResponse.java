package com.amx.jax.api;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.amx.jax.api.AmxResponseSchemes.ApiMetaResponse;
import com.amx.jax.exception.IExceptionEnum;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

public abstract class AResponse<M> implements ApiMetaResponse<M> {

	protected Long timestamp;

	// Spring Norms
	protected String status = "200"; // 400
	protected String error; // Bad Request
	protected String exception; // org.springframework.http.converter.HttpMessageNotReadableException
	protected String message;// JSON parse error

	@ApiModelProperty(example = "/postman/email/send")
	protected String path;

	@ApiModelProperty(example = "/go/to/some/other/url.html")
	protected String redirectUrl;

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	protected String messageKey;

	/** The status key. */
	protected String statusKey = "SUCCESS";

	// Amx Specs
	protected M meta;
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
	@Override
	public Long getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp.
	 *
	 * @param timestamp the new timestamp
	 */
	@Override
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * HTTP Status Code : 400
	 * 
	 * @return
	 */
	@Override
	public String getStatus() {
		return status;
	}

	/**
	 * HTTP Status Code : 400
	 * 
	 * @param status
	 */
	@Override
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
	@Override
	public String getStatusKey() {
		return statusKey;
	}

	/**
	 * Sets the status key.
	 *
	 * @param statusKey the new status key
	 */
	@Override
	public void setStatusKey(String statusKey) {
		this.statusKey = statusKey;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	@JsonIgnore
	public void setHttpStatus(HttpStatus status) {
		if (status.is5xxServerError() || status.is4xxClientError() || status.is3xxRedirection()) {
			this.statusKey = status.series().name();
			this.error = status.getReasonPhrase();
		}
		this.status = ArgUtil.parseAsString(status.value());

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
	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * Set Exception Message (Spring Norms)
	 * 
	 * @param message
	 */
	@Override
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

	@Override
	public M getMeta() {
		return meta;
	}

	@Override
	public void setMeta(M meta) {
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
	 * @param errors the new errors
	 */
	public void setErrors(List<AmxFieldError> errors) {
		this.errors = errors;
	}

	@Override
	public String getMessageKey() {
		return messageKey;
	}

	@Override
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

}
