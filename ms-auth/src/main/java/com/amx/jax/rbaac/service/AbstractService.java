package com.amx.jax.rbaac.service;

import java.util.ArrayList;
import java.util.List;

//@SuppressWarnings("rawtypes")
public abstract class AbstractService {

	public static class ApiResponse {
		// Dummy Classes
		public ResponseData getData() {
			return null;
		}

		public void setData(ResponseData data) {
		}
	}

	public static class ResponseData {
		// Dummy Classes
		public List<Object> getValues() {
			return null;
		}

		public void setType(String string) {
		}

		public void setValues(List<Object> values) {
		}
	}

	public static class BooleanResponse {
		public BooleanResponse(boolean status) {
		}

	}

	protected ApiResponse getBlackApiResponse() {
		ApiResponse response = new ApiResponse();
		ResponseData data = new ResponseData();
		data.setType(getModelType());
		List<Object> values = new ArrayList<Object>();
		data.setValues(values);
		response.setData(data);
		return response;
	}

	public abstract String getModelType();

	public Class<?> getModelClass() {
		return this.getClass();
	}

	public ApiResponse getBooleanResponse() {
		ApiResponse apiResponse = getBlackApiResponse();
		BooleanResponse output = new BooleanResponse(true);
		apiResponse.getData().getValues().add(output);
		apiResponse.getData().setType("boolean_response");
		return apiResponse;
	}

	public ApiResponse getBooleanResponse(boolean status) {
		ApiResponse apiResponse = getBlackApiResponse();
		BooleanResponse output = new BooleanResponse(status);
		apiResponse.getData().getValues().add(output);
		apiResponse.getData().setType("boolean_response");
		return apiResponse;
	}

}
