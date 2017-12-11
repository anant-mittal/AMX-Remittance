package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.BENE_API_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.amx.amxlib.meta.model.BeneCountryDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.client.util.ConverterUtility;

public class BeneClinet extends AbstractJaxServiceClient{
	private Logger log = Logger.getLogger(BeneClinet.class);
	
	@Autowired
	private JaxMetaInfo jaxMetaInfo;
	
	@Autowired
	private ConverterUtility util;
	
	
	//benelist
	public ApiResponse<BeneCountryDTO> getBeneficiaryList(String userType,BigDecimal beneCountryId) {
		ResponseEntity<ApiResponse<BeneCountryDTO>> response = null;
		try {
			BigDecimal countryId  = jaxMetaInfo.getCountryId();
			BigDecimal customerId = jaxMetaInfo.getCustomerId();
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			StringBuffer sb = new StringBuffer();
			sb.append("?customerId=").append(customerId).append("&userType=").append(userType).append("&countryId=").append(countryId).append("&beneCountryId=").append(beneCountryId);
			log.info("Bene Clinet to get bene list Input String :"+sb.toString());
			String url =baseUrl.toString()+ BENE_API_ENDPOINT+"/beneList/"+sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<BeneCountryDTO>>(){});
		} catch (Exception e) {
			log.debug("Bene country list ", e);
		}
		return response.getBody();
	}

	
	
	public ApiResponse<BeneCountryDTO> getBeneficiaryCountryList(String userType,BigDecimal beneCountryId) {
		ResponseEntity<ApiResponse<BeneCountryDTO>> response = null;
		try {
			BigDecimal countryId  = jaxMetaInfo.getCountryId();
			BigDecimal customerId = jaxMetaInfo.getCustomerId();
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			StringBuffer sb = new StringBuffer();
			sb.append("?customerId=").append(customerId).append("&userType=").append(userType).append("&countryId=").append(countryId).append("&beneCountryId=").append(beneCountryId);
			log.info("Bene Clinet to get bene list Input String :"+sb.toString());
			String url =baseUrl.toString()+ BENE_API_ENDPOINT+"/benecountry/"+sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<BeneCountryDTO>>(){});
		} catch (Exception e) {
			log.debug("Bene country list ", e);
		}
		return response.getBody();
	}
	
	
}
