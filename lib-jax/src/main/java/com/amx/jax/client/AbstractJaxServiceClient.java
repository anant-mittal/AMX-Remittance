package com.amx.jax.client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.amx.jax.client.AbstractJaxServiceClient;
import com.amx.jax.client.configs.JaxConfig;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.scope.TenantContextHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public abstract class AbstractJaxServiceClient {

	private static final Logger LOGGER = Logger.getLogger(AbstractJaxServiceClient.class);

	@Autowired
	protected RestTemplate restTemplate;

	@Autowired(required=false)
	protected JaxMetaInfo jaxMetaInfo;

	@Autowired
	JaxConfig jaxConfig;

	public String getBaseUrl() {
		return jaxConfig.getSpServiceUrl();
	}

	protected HttpHeaders getHeader() {

		HttpHeaders headers = new HttpHeaders();
		try {
			setMetaInfo();
			headers.add("meta-info", new ObjectMapper().writeValueAsString(jaxMetaInfo.copy()));
		} catch (JsonProcessingException e) {
			LOGGER.error("error in getheader of jaxclient", e);
		}
		return headers;
	}

	private void setMetaInfo() {

		jaxMetaInfo.setCountryId(TenantContextHolder.currentSite().getBDCode());
		jaxMetaInfo.setTenant(TenantContextHolder.currentSite());
	}

}
