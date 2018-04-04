package com.amx.jax.trnx;

import java.util.ArrayList;
import java.util.List;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.JaxTransactionResponse;
import com.amx.amxlib.model.response.ResponseData;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.cache.TransactionModel;

public abstract class JaxTransactionManager<T> extends TransactionModel<T> {

	@SuppressWarnings("rawtypes")
	protected ApiResponse getJaxTransactionApiResponse() {
		ApiResponse response = new ApiResponse();
		ResponseData data = new ResponseData();
		List<Object> values = new ArrayList<Object>();
		data.setValues(values);
		response.setData(data);
		JaxTransactionResponse jaxTransactionResponse = new JaxTransactionResponse(true, getTranxId());
		response.getData().getValues().add(jaxTransactionResponse);
		response.getData().setType(jaxTransactionResponse.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}
	
	protected ApiResponse getBlankApiResponse() {
		ApiResponse response = new ApiResponse();
		ResponseData data = new ResponseData();
		List<Object> values = new ArrayList<Object>();
		data.setValues(values);
		response.setData(data);
		response.setResponseStatus(ResponseStatus.OK);
		return response;
}
}
