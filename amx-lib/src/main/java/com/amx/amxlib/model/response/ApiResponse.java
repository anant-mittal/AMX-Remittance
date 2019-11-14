package com.amx.amxlib.model.response;

import java.util.ArrayList;
import java.util.List;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.serviceprovider.RoutingBankMasterDTO;
import com.amx.jax.exception.AmxApiError;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ApiResponse<T> {

	private ResponseData data;

	private List<AmxApiError> error;

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

	public List<AmxApiError> getError() {
		return error;
	}

	public void setError(List<AmxApiError> error) {
		this.error = error;
	}

	@SuppressWarnings("unchecked")
	@JsonIgnore
	public List<T> getResults() {
		List<T> result = new ArrayList<>();
		if (this.getData() != null && this.getData().getValues() != null && this.getData().getValues().size() > 0) {
			List<Object> values = this.getData().getValues();

			for (Object value : values) {
				result.add((T) value);
			}
		}
		return result;
	}

	@JsonIgnore
	public T getResult() {
		List<T> results = getResults();
		T result = null;
		if (results != null && !results.isEmpty()) {
			result = results.get(0);
		}
		return result;

	}

	public static <T> ApiResponse<T> getBlackApiResponse() {
		ApiResponse<T> response = new ApiResponse<T>();
		ResponseData data = new ResponseData();
		data.setType(null);
		List<Object> values = new ArrayList<Object>();
		data.setValues(values);
		response.setData(data);
		return response;
	}

	public AmxApiResponse<T, Object> toAmxApiResponse(){
		return AmxApiResponse.buildList(this.getResults());
	}
	
}
