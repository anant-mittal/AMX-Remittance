package com.amx.jax.client;

import static com.amx.jax.constant.ApiEndpoint.USER_API_ENDPOINT;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.amx.jax.model.response.ApiResponse;
import com.amx.jax.userservice.model.AbstractUserModel;

@Component
public class UserClient extends AbstractSPServiceClient {

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

}
