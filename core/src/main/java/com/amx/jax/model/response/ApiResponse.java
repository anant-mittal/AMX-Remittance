package com.amx.jax.model.response;

import java.util.List;


public class ApiResponse {

	private ResponseData data;

	private List<ApiError> error;

	private ResponseStatus responseStatus;

	public ResponseStatus getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(ResponseStatus responseStatus) {
		this.responseStatus = responseStatus;
	}

	public ResponseData getData() {
		return data;
	}

	public void setData(ResponseData data) {
		this.data = data;
	}

	public List<ApiError> getError() {
		return error;
	}

	public void setError(List<ApiError> error) {
		this.error = error;
	}

}
