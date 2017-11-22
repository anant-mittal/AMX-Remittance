package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.USER_API_ENDPOINT;

import java.net.URI;

import static com.amx.amxlib.constant.ApiEndpoint.CUSTOMER_ENDPOINT;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.amx.amxlib.model.AbstractUserModel;
import com.amx.amxlib.model.response.ApiResponse;

@Component
public class UserClient extends AbstractJaxServiceClient {

	private Logger log = Logger.getLogger(getClass());

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
		ApiResponse response = null;
		try {
			log.info("calling sendOtpForCivilId api: ");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			headers.add("meta-info", "{\"country-id\":91}");
			String sendOtpUrl = baseUrl.toString() + CUSTOMER_ENDPOINT + "/" + civilId + "/send-otp/";
			response = restTemplate.getForObject(sendOtpUrl, ApiResponse.class);
		} catch (Exception e) {
			log.debug("exception in sendOtpForCivilId ", e);
		}
		return response;
	}

}
