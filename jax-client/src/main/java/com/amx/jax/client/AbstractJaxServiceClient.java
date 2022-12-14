package com.amx.jax.client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.amx.jax.AppConfig;
import com.amx.jax.client.config.JaxConfig;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.rest.IMetaRequestOutFilter;
import com.amx.jax.scope.TenantContextHolder;
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
	protected JaxConfig jaxConfig;

	@Autowired
	protected AppConfig appConfig;

	@Autowired(required = false)
	protected IMetaRequestOutFilter<JaxMetaInfo> metaFilter;

	public String getBaseUrl() {
		return jaxConfig.getSpServiceUrl();
	}

	@Deprecated
	protected HttpHeaders getHeader() {
		return getHeader(jaxMetaInfo);
	}

	@Deprecated
	protected HttpHeaders getHeader(JaxMetaInfo jaxMetaInfoLocal) {

		HttpHeaders headers = new HttpHeaders();
		try {
			setMetaInfo(jaxMetaInfoLocal);
			headers.add("meta-info", new ObjectMapper().writeValueAsString(jaxMetaInfoLocal.copy()));
		} catch (JsonProcessingException e) {
			LOGGER.error("error in getheader of jaxclient", e);
		}
		return headers;
	}

	private void setMetaInfo(JaxMetaInfo jaxMetaInfoLocal) {

		jaxMetaInfoLocal.setCountryId(TenantContextHolder.currentSite().getBDCode());
		jaxMetaInfoLocal.setTenant(TenantContextHolder.currentSite());
	}

}
