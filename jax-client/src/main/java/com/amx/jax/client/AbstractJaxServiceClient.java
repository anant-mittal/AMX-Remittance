package com.amx.jax.client;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public abstract class AbstractJaxServiceClient {

	@Autowired
	protected RestTemplate restTemplate;

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	@Qualifier("base_url")
	protected URL baseUrl;

	protected MultiValueMap<String, String> getHeader() {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("meta-info", "{\"country-id\":" + jaxMetaInfo.getCountryId().intValue() + "}");
		headers.add("meta-info", "{\"company-id\":" + jaxMetaInfo.getCompanyId().intValue() + "}");
		return headers;
	}

}
