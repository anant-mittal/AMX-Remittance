package com.amx.jax.client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.client.util.ConverterUtility;

@Component
@SuppressWarnings("rawtypes")
public class JaxConfigClient extends AbstractJaxServiceClient {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	private ConverterUtility util;

	public ApiResponse<BooleanResponse> createorUpdateOtpSettings(OtpSettings otpSettings) {
		ResponseEntity<ApiResponse<BooleanResponse>> response = null;
		String url = baseUrl.toString() + "/config/";
		HttpEntity<OtpSettings> requestEntity = new HttpEntity<OtpSettings>(otpSettings, getHeader());
		response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
				});
		return response.getBody();
	}

}
