package com.amx.jax.trnx;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.JaxTransactionResponse;
import com.amx.amxlib.model.response.ResponseData;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.cache.TransactionModel;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.JaxChannel;
import com.amx.jax.meta.MetaData;

public abstract class JaxTransactionManager<T> extends TransactionModel<T> {

	@Autowired
	MetaData metaData;

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

	/**
	 * @return Trnx created by user
	 */
	protected String getCreatedBy() {
		JaxChannel channel = metaData.getChannel();
		String user = ConstantDocument.JOAMX_USER;
		if (JaxChannel.ONLINE.equals(channel)) {
			user = metaData.getCustomerId() == null ? ConstantDocument.JOAMX_USER : metaData.getCustomerId().toString();
		}
		return user;
	}
	
	public abstract String getJaxTransactionId();
}
