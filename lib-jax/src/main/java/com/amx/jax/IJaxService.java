package com.amx.jax;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.model.response.PurposeOfTransactionDto;
import com.amx.jax.scope.TenantContextHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface IJaxService {

	static final Logger LOGGER = Logger.getLogger(IJaxService.class);

	default HttpHeaders getHeader() {

		HttpHeaders headers = new HttpHeaders();
		try {

			JaxMetaInfo metaInfo = new JaxMetaInfo();
			metaInfo.setCountryId(TenantContextHolder.currentSite().getBDCode());
			metaInfo.setTenant(TenantContextHolder.currentSite());
			headers.add("meta-info", new ObjectMapper().writeValueAsString(metaInfo.copy()));
		} catch (JsonProcessingException e) {
			LOGGER.error("error in getheader of jaxclient", e);
		}
		return headers;
	}
}
