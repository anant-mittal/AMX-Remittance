package com.amx.jax.postman.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.rest.RestService;
import com.amx.utils.ArgUtil;

@Component
public class GoogleService {

	private String googleSecret = "6LdtFEMUAAAAAKAhPVOk7iOA8SPnaOLGV9lFIqMJ";

	@Autowired
	RestService restService;

	public Boolean verifyCaptcha(String responseKey, String remoteIP) {
		@SuppressWarnings("unchecked")
		Map<String, Object> resp = restService.ajax("https://www.google.com/recaptcha/api/siteverify").acceptJson()
				.field("secret", googleSecret).field("response", responseKey).field("remoteip", remoteIP).postForm()
				.as(Map.class);
		if (resp != null) {
			return ArgUtil.parseAsBoolean(resp.get("success"));
		}
		return false;

	}
}
