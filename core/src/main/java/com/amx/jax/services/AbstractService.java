package com.amx.jax.services;

import java.util.ArrayList;
import java.util.List;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseData;

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

	public Class<?> getModelClass(){
		return this.getClass();
	}

}
