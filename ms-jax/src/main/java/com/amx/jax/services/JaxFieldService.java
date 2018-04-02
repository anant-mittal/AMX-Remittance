package com.amx.jax.services;

import org.springframework.stereotype.Service;

import com.amx.amxlib.constant.JaxFieldEntity;
import com.amx.amxlib.model.request.GetJaxFieldRequest;
import com.amx.amxlib.model.response.ApiResponse;

@Service
public class JaxFieldService extends AbstractService {

	@Override
	public String getModelType() {
		return "jax-field";
	}

	public ApiResponse getJaxFieldsForEntity(GetJaxFieldRequest request) {
		ApiResponse apiResponse = getBlackApiResponse();

		JaxFieldEntity entity = request.getEntity();
		
		return apiResponse;
	}

}
