package com.amx.jax.client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
	protected JaxMetaInfo jaxMetaInfo;

	@Autowired
	JaxConfig jaxConfig;

	public String getBaseUrl() {
		return jaxConfig.getSpServiceUrl();
	}

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
			info.setTenant(jaxMetaInfo.getTenant());
			LOGGER.info("device ip  ---> " + jaxMetaInfo.getDeviceIp());
			info.setDeviceId(jaxMetaInfo.getDeviceId());
			info.setDeviceIp(jaxMetaInfo.getDeviceIp());
			info.setReferrer(jaxMetaInfo.getReferrer());
			LOGGER.info("Tenant id --> " + jaxMetaInfo.getTenant());
			LOGGER.info("Referal id --> " + jaxMetaInfo.getReferrer()+"\t Device Type :"+jaxMetaInfo.getDeviceType());
			info.setReferrer(jaxMetaInfo.getReferrer());
			info.setDeviceType(jaxMetaInfo.getDeviceType());
			info.setAppType(jaxMetaInfo.getAppType());
			headers.add("meta-info", new ObjectMapper().writeValueAsString(info));
		} catch (JsonProcessingException e) {
		    LOGGER.error("error in getheader of jaxclient", e);
		}
		return headers;
	}

}
