package com.amx.jax.client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.amx.jax.AppConstants;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.client.config.JaxConfig;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public abstract class AbstractJaxServiceClient {

	private static final Logger LOGGER = Logger.getLogger(AbstractJaxServiceClient.class);

	@Autowired
	protected RestTemplate restTemplate;

	@Autowired
	protected JaxMetaInfo jaxMetaInfo;

	@Autowired
	JaxConfig jaxConfig;

	public String getBaseUrl() {
		return jaxConfig.getSpServiceUrl();
	}

	protected HttpHeaders getHeader() {

		HttpHeaders headers = new HttpHeaders();
		try {
			headers.add("meta-info", new ObjectMapper().writeValueAsString(jaxMetaInfo.copy()));
			headers.add(AppConstants.TRANX_ID_XKEY,
					ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.TRANX_ID_XKEY)));
		} catch (JsonProcessingException e) {
			LOGGER.error("error in getheader of jaxclient", e);
		}
		return headers;
	}

}
