package com.amx.jax.client;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.ICustRegService;
import com.amx.jax.api.ARespModel;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.rest.RestService;

@Component
public class CustRegClient implements ICustRegService {

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<BigDecimal, Object> getModes() {
		return restService.ajax(appConfig.getJaxURL()).path(CustRegApiEndPoints.GET_ID_FIELDS).get()
				.asApiResponse(BigDecimal.class);
	}

	@Override
	public AmxApiResponse<ARespModel, Object> getIdDetailsFields(RegModeModel regModeModel) {
		return restService.ajax(appConfig.getJaxURL()).path(CustRegApiEndPoints.GET_ID_FIELDS).post(regModeModel)
				.asApiResponse(ARespModel.class);
	}

}
