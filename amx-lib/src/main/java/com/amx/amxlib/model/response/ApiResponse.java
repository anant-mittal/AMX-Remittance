package com.amx.amxlib.model.response;

import java.util.ArrayList;
import java.util.List;

public class ApiResponse<T> {

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

	@SuppressWarnings("unchecked")
	public List<T> getResults() {
		List<T> result = null;
		if (this.getData() != null && this.getData().getValues() != null && this.getData().getValues().size() > 0) {
			List<Object> values = this.getData().getValues();
			result = new ArrayList<>();
			for (Object value : values) {
				result.add((T) value);
			}
		}
		return result;
	}

	public T getResult() {
		List<T> results = getResults();
		T result = null;
		if (results != null) {
			result = results.get(0);
		}
		return result;

	}
}
