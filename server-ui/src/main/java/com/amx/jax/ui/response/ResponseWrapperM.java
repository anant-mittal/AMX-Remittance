package com.amx.jax.ui.response;

import java.io.Serializable;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.jax.ui.UIConstants;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

/**
 * The Class ResponseWrapperM.
 *
 * @param <T>
 *            the generic type
 * @param <M>
 *            the generic type
 */
public class ResponseWrapperM<T, M> implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7545829974699803746L;

	/** The timestamp. */
	private Long timestamp = null;

	/** The status. */
	private String status = "200";

	/** The status key. */
	private WebResponseStatus statusKey = WebResponseStatus.SUCCESS;

	/** The message. */
	private String message = UIConstants.EMPTY;

	/** The message key. */
	private String messageKey = UIConstants.EMPTY;

	/** The redirect url. */
	private String redirectUrl = null;

	/** The exception. */
	private String exception = null;

	/** The data. */
	private T data = null;

	/** The meta. */
	private M meta = null;

	/** The errors. */
	private List<ResponseError> errors = null;

	/**
	 * Instantiates a new response wrapper M.
	 */
	public ResponseWrapperM() {
		super();
		this.timestamp = System.currentTimeMillis();
		this.traceId = ContextUtil.getTraceId();
	}

	/**
	 * Instantiates a new response wrapper M.
	 *
	 * @param data
	 *            the data
	 */
	public ResponseWrapperM(T data) {
		this();
		this.data = data;
	}

	/**
	 * Instantiates a new response wrapper M.
	 *
	 * @param data
	 *            the data
	 * @param meta
	 *            the meta
	 */
	public ResponseWrapperM(T data, M meta) {
		this(data);
		this.meta = meta;
	}

	/** The trace id. */
	private String traceId = null;

	/**
	 * Gets the trace id.
	 *
	 * @return the trace id
	 */
	public String getTraceId() {
		return traceId;
	}

	/**
	 * Sets the trace id.
	 *
	 * @param traceId
	 *            the new trace id
	 */
	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	/**
	 * Gets the errors.
	 *
	 * @return the errors
	 */
	public List<ResponseError> getErrors() {
		return errors;
	}

	/**
	 * Sets the errors.
	 *
	 * @param errors
	 *            the new errors
	 */
	public void setErrors(List<ResponseError> errors) {
		this.errors = errors;
	}

	/**
	 * Gets the redirect url.
	 *
	 * @return the redirect url
	 */
	public String getRedirectUrl() {
		return redirectUrl;
	}

	/**
	 * Sets the redirect url.
	 *
	 * @param redirectUrl
	 *            the new redirect url
	 */
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	/**
	 * Gets the message key.
	 *
	 * @return the message key
	 */
	public String getMessageKey() {
		return messageKey;
	}

	/**
	 * Sets the message key.
	 *
	 * @param messageKey
	 *            the new message key
	 */
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * Sets the data.
	 *
	 * @param data
	 *            the new data
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * Update data.
	 *
	 * @param data
	 *            the data
	 * @return the response wrapper M
	 */
	public ResponseWrapperM<T, M> updateData(T data) {
		this.setData(data);
		return this;
	}

	/**
	 * Update meta.
	 *
	 * @param meta
	 *            the meta
	 * @return the response wrapper M
	 */
	public ResponseWrapperM<T, M> updateMeta(M meta) {
		this.setMeta(meta);
		return this;
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
	 * Gets the status key.
	 *
	 * @return the status key
	 */
	public WebResponseStatus getStatusKey() {
		return statusKey;
	}

	/**
	 * Sets the status key.
	 *
	 * @param statusKey
	 *            the new status key
	 */
	public void setStatusKey(WebResponseStatus statusKey) {
		this.statusKey = statusKey;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status
	 *            the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status
	 *            the new status
	 */
	public void setStatus(HttpStatus status) {
		if (status.is5xxServerError()) {
			this.statusKey = WebResponseStatus.SERVER_ERROR;
		} else if (status.is4xxClientError()) {
			this.statusKey = WebResponseStatus.CLIENT_ERROR;
		} else if (status.is3xxRedirection()) {
			this.statusKey = WebResponseStatus.REDIRECTION;
		}
		this.status = ArgUtil.parseAsString(status.value());
		this.message = status.getReasonPhrase();
	}

	/**
	 * Sets the status.
	 *
	 * @param status
	 *            the new status
	 */
	public void setStatus(WebResponseStatus status) {
		this.statusKey = status;
		this.status = status.getCode();
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message
	 *            the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Sets the message.
	 *
	 * @param status
	 *            the new message
	 */
	public void setMessage(WebResponseStatus status) {
		this.setStatus(status);
	}

	/**
	 * Sets the message.
	 *
	 * @param status
	 *            the status
	 * @param message
	 *            the message
	 */
	public void setMessage(WebResponseStatus status, String message) {
		this.setStatus(status);
		this.message = message;
	}

	/**
	 * Sets the message.
	 *
	 * @param status
	 *            the status
	 * @param responseMessage
	 *            the response message
	 */
	public void setMessage(WebResponseStatus status, ResponseMessage responseMessage) {
		this.setStatus(status);
		this.messageKey = responseMessage.toString();
		this.message = responseMessage.getMessage();
	}

	/**
	 * Sets the message.
	 *
	 * @param status
	 *            the status
	 * @param messageKey
	 *            the message key
	 * @param message
	 *            the message
	 */
	public void setMessage(WebResponseStatus status, String messageKey, String message) {
		this.setStatus(status);
		this.messageKey = messageKey;
		this.message = message;
	}

	/**
	 * Sets the message.
	 *
	 * @param status
	 *            the status
	 * @param jaxError
	 *            the jax error
	 * @param message
	 *            the message
	 */
	public void setMessage(WebResponseStatus status, JaxError jaxError, String message) {
		this.setStatus(status);
		this.messageKey = jaxError.toString();
		this.message = message;
	}

	/**
	 * Sets the message.
	 *
	 * @param status
	 *            the status
	 * @param jaxExcep
	 *            the jax excep
	 */
	public void setMessage(WebResponseStatus status, AbstractJaxException jaxExcep) {
		this.setMessage(status, jaxExcep.getErrorKey(), jaxExcep.getErrorMessage());
	}

	/**
	 * Sets the message.
	 *
	 * @param status
	 *            the status
	 * @param excep
	 *            the excep
	 */
	public void setMessage(WebResponseStatus status, Exception excep) {
		this.setStatus(status);
		this.message = excep.getMessage();
		this.exception = excep.getClass().getName();
	}

	/**
	 * Gets the meta.
	 *
	 * @return the meta
	 */
	public M getMeta() {
		return meta;
	}

	/**
	 * Sets the meta.
	 *
	 * @param meta
	 *            the new meta
	 */
	public void setMeta(M meta) {
		this.meta = meta;
	}

	/**
	 * Gets the exception.
	 *
	 * @return the exception
	 */
	public String getException() {
		return exception;
	}

	/**
	 * Sets the exception.
	 *
	 * @param exception
	 *            the new exception
	 */
	public void setException(String exception) {
		this.exception = exception;
	}

}
