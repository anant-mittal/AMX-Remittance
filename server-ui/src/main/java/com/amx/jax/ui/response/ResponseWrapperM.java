package com.amx.jax.ui.response;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.error.JaxError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.ui.UIConstants;
import com.amx.utils.ContextUtil;

/**
 * The Class ResponseWrapperM.
 *
 * @param <T>
 *            the generic type
 * @param <M>
 *            the generic type
 */
public class ResponseWrapperM<T, M> extends AmxApiResponse<T, M> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7545829974699803746L;

	/** The message key. */
	private String messageKey = UIConstants.EMPTY;

	/**
	 * Instantiates a new response wrapper M.
	 */
	public ResponseWrapperM() {
		super();
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
	 * Sets the status.
	 *
	 * @param status
	 *            the new status
	 */
	public void setStatus(WebResponseStatus status) {
		this.statusKey = status.name();
		this.status = status.getCode();
	}

	public void setStatusKey(WebResponseStatus error) {
		this.statusKey = error.name();
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
	@SuppressWarnings("unchecked")
	public void setMessage(WebResponseStatus status, AmxApiException jaxExcep) {
		this.setMessage(status, jaxExcep.getErrorKey(), jaxExcep.getErrorMessage());
		this.updateMeta((M) jaxExcep.getMeta());
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

}
