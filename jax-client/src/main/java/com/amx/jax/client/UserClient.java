package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.USER_API_ENDPOINT;

import java.net.URI;

import static com.amx.amxlib.constant.ApiEndpoint.CUSTOMER_ENDPOINT;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.amx.amxlib.model.AbstractUserModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.client.util.ConverterUtility;

@Component
public class UserClient extends AbstractJaxServiceClient {

	private Logger log = Logger.getLogger(getClass());
	@Autowired
	private ConverterUtility util;

	public ApiResponse registerUser(AbstractUserModel model) {
		ApiResponse response = null;
		try {
			log.info("calling registeruser api: ");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			headers.add("meta-info", "{\"country-id\":91}");
			HttpEntity<AbstractUserModel> entity = new HttpEntity<AbstractUserModel>(model, headers);
			response = restTemplate.postForObject(baseUrl.toString() + USER_API_ENDPOINT, entity, ApiResponse.class);
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}

	public ApiResponse sendOtpForCivilId(String civilId) {
		ResponseEntity<ApiResponse> response = null;
		try {
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			headers.add("meta-info", "{\"country-id\":91}");
			HttpEntity<AbstractUserModel> requestEntity = new HttpEntity<AbstractUserModel>(headers);
			String sendOtpUrl = baseUrl.toString() + CUSTOMER_ENDPOINT + "/" + civilId + "/send-otp/";
			log.info("calling sendOtpForCivilId api: " + sendOtpUrl);
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.GET, requestEntity, ApiResponse.class);
			log.info("responce from  sendOtpForCivilId api: " + util.marshall(response.getBody()));
		} catch (Exception e) {
			log.debug("exception in sendOtpForCivilId ", e);
		}
		return response.getBody();
	}

}
