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

	private Logger log = Logger.getLogger(AbstractJaxServiceClient.class);

	@Autowired
	protected RestTemplate restTemplate;

	@Autowired
	protected JaxMetaInfo jaxMetaInfo;

	@Autowired
	JaxConfig jaxConfig;

	public String getBaseUrl() {
		log.info("getBaseUrl:BASE URL IS BEING READ");
		return jaxConfig.getSpServiceUrl(); // jaxServiceUrl.toString();
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
			log.info("device ip  ---> " + jaxMetaInfo.getDeviceIp());
			info.setDeviceId(jaxMetaInfo.getDeviceId());
			info.setDeviceIp(jaxMetaInfo.getDeviceIp());
			info.setTenant(jaxMetaInfo.getTenant());
			log.info("Tenant id --> " + jaxMetaInfo.getTenant());
			log.info("Referal id --> " + jaxMetaInfo.getReferrer()+"\t Device Type :"+jaxMetaInfo.getDeviceType());
			info.setReferrer(jaxMetaInfo.getReferrer());
			info.setDeviceType(jaxMetaInfo.getDeviceType());
			headers.add("meta-info", new ObjectMapper().writeValueAsString(info));
		} catch (JsonProcessingException e) {
			log.error("error in getheader of jaxclient", e);
		}
		return headers;
	}

}
