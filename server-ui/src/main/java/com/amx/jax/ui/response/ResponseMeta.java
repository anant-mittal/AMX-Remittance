package com.amx.jax.ui.response;

import java.util.Arrays;
import java.util.List;

import com.amx.amxlib.error.JaxError;

/**
 * The Class ResponseMeta.
 */
public class ResponseMeta {

	/**
	 * Instantiates a new response meta.
	 */
	public ResponseMeta() {
		super();
		responseMessage = Arrays.asList(ResponseMessage.values());
		responseStatus = Arrays.asList(WebResponseStatus.values());
		jaxError = Arrays.asList(JaxError.values());
	}

	/** The response message. */
	List<ResponseMessage> responseMessage;

	/** The response status. */
	List<WebResponseStatus> responseStatus;

	/** The jax error. */
	List<JaxError> jaxError;

	/**
	 * Gets the response message.
	 *
	 * @return the response message
	 */
	public List<ResponseMessage> getResponseMessage() {
		return responseMessage;
	}

	/**
	 * Sets the response message.
	 *
	 * @param responseMessage
	 *            the new response message
	 */
	public void setResponseMessage(List<ResponseMessage> responseMessage) {
		this.responseMessage = responseMessage;
	}

	/**
	 * Gets the response status.
	 *
	 * @return the response status
	 */
	public List<WebResponseStatus> getResponseStatus() {
		return responseStatus;
	}

	/**
	 * Sets the response status.
	 *
	 * @param responseStatus
	 *            the new response status
	 */
	public void setResponseStatus(List<WebResponseStatus> responseStatus) {
		this.responseStatus = responseStatus;
	}

	/**
	 * Gets the jax error.
	 *
	 * @return the jax error
	 */
	public List<JaxError> getJaxError() {
		return jaxError;
	}

	/**
	 * Sets the jax error.
	 *
	 * @param jaxError
	 *            the new jax error
	 */
	public void setJaxError(List<JaxError> jaxError) {
		this.jaxError = jaxError;
	}

}
