package com.amx.jax.auth.service;

import java.util.ArrayList;
import java.util.List;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.amxlib.model.response.ResponseData;

@SuppressWarnings("rawtypes")
public abstract class AbstractService {

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
