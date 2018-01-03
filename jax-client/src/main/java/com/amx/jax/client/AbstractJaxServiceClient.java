package com.amx.jax.client;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.AlreadyExistsException;
import com.amx.amxlib.exception.CustomerValidationException;
import com.amx.amxlib.exception.IncorrectInputException;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.RemittanceTransactionValidationException;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.model.response.ApiError;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.client.config.JaxConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public abstract class AbstractJaxServiceClient {

	@Autowired
	protected RestTemplate restTemplate;

	@Autowired
	protected JaxMetaInfo jaxMetaInfo;

	@Autowired
	@Qualifier("base_url")
	protected URL baseUrl;

	private Logger log = Logger.getLogger(AbstractJaxServiceClient.class);

	protected HttpHeaders getHeader() {

		HttpHeaders headers = new HttpHeaders();
		try {
			JaxMetaInfo info = new JaxMetaInfo();
			info.setCountryId(jaxMetaInfo.getCountryId());
			info.setChannel(jaxMetaInfo.getChannel());
			info.setCompanyId(jaxMetaInfo.getCompanyId());
			info.setCustomerId(jaxMetaInfo.getCustomerId());
			info.setLanguageId(jaxMetaInfo.getLanguageId());
			info.setCountryBranchId(jaxMetaInfo.getCountryBranchId());
			headers.add("meta-info", new ObjectMapper().writeValueAsString(info));
		} catch (JsonProcessingException e) {
			log.error("error in getheader of jaxclient", e);
		}
		return headers;
	}
	
}
