package com.amx.jax.client;

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

	public ApiResponse validateOtp(String civilId, String otp) {
		ResponseEntity<ApiResponse> response = null;
		try {
			log.info("calling validateOtp api: ");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			headers.add("meta-info", "{\"country-id\":91}");
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			String validateOtpUrl = baseUrl.toString() + CUSTOMER_ENDPOINT + "/" + civilId + "/validate-otp/";
			response = restTemplate.exchange(validateOtpUrl, HttpMethod.GET, requestEntity, ApiResponse.class);
		} catch (Exception e) {
			log.error("exception in validateOtp ", e);
		}
		return response.getBody();
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
			log.error("exception in sendOtpForCivilId ", e);
		}
		return response.getBody();
	}

	public ApiResponse saveCustomer(String json) {
		ResponseEntity<ApiResponse> response = null;
		try {
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			headers.add("meta-info", "{\"country-id\":91}");
			HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
			String sendOtpUrl = baseUrl.toString() + CUSTOMER_ENDPOINT;
			log.info("calling saveCustomer api: " + sendOtpUrl);
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.POST, requestEntity, ApiResponse.class);
			log.info("responce from  saveCustomer api: " + util.marshall(response.getBody()));
		} catch (Exception e) {
			log.error("exception in saveCustomer ", e);
		}
		return response.getBody();
	}

}
