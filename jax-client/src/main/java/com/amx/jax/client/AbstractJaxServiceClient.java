package com.amx.jax.client;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.amx.jax.AppService;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.client.config.JaxConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public abstract class AbstractJaxServiceClient {

	private static final Logger LOGGER = Logger.getLogger(AbstractJaxServiceClient.class);

	@Autowired
	protected RestTemplate restTemplate;

	@Autowired
	protected AppService appService;

	@Autowired
	protected JaxMetaInfo jaxMetaInfo;

	@Autowired
	JaxConfig jaxConfig;

	public String getBaseUrl() {
		LOGGER.info("getBaseUrl:BASE URL IS BEING READ");
		return jaxConfig.getSpServiceUrl();
	}

	protected HttpHeaders getHeader() {

		HttpHeaders headers = new HttpHeaders();
		try {
			Map<String, String> map = appService.header();
			for (Entry<String, String> item : map.entrySet()) {
				headers.add(item.getKey(), item.getValue());
			}
			headers.add("meta-info", new ObjectMapper().writeValueAsString(jaxMetaInfo.copy()));
		} catch (JsonProcessingException e) {
			LOGGER.error("error in getheader of jaxclient", e);
		}
		return headers;
	}

}
