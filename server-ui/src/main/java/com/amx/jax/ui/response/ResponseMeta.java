package com.amx.jax.ui.response;

import java.util.Arrays;
import java.util.List;

import com.amx.amxlib.error.JaxError;

public class ResponseMeta {

	public ResponseMeta() {
		super();
		responseMessage = Arrays.asList(ResponseMessage.values());
		responseStatus = Arrays.asList(ResponseStatus.values());
		jaxError = Arrays.asList(JaxError.values());
	}

	List<ResponseMessage> responseMessage;
	List<ResponseStatus> responseStatus;
	List<JaxError> jaxError;

	public List<ResponseMessage> getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(List<ResponseMessage> responseMessage) {
		this.responseMessage = responseMessage;
	}

	public List<ResponseStatus> getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(List<ResponseStatus> responseStatus) {
		this.responseStatus = responseStatus;
	}

	public List<JaxError> getJaxError() {
		return jaxError;
	}

	public void setJaxError(List<JaxError> jaxError) {
		this.jaxError = jaxError;
	}

}
