package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.BENE_API_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.amx.amxlib.meta.model.AccountTypeDto;
import com.amx.amxlib.meta.model.BeneCountryDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.client.util.ConverterUtility;

@Component
public class BeneClient extends AbstractJaxServiceClient {
	private Logger log = Logger.getLogger(BeneClient.class);

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	private ConverterUtility util;

	/**
	 * sdsd
	 * 
	 * @param beneCountryId
	 * @return
	 */
	public ApiResponse<BeneCountryDTO> getBeneficiaryList(BigDecimal beneCountryId) {
		ResponseEntity<ApiResponse<BeneCountryDTO>> response = null;
		try {
			String userType = jaxMetaInfo.getChannel().toString();
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal customerId = jaxMetaInfo.getCustomerId();
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			StringBuffer sb = new StringBuffer();
			sb.append("?customerId=").append(customerId).append("&userType=").append(userType).append("&countryId=")
					.append(countryId).append("&beneCountryId=").append(beneCountryId);
			log.info("Bene Clinet to get bene list Input String :" + sb.toString());
			String url = baseUrl.toString() + BENE_API_ENDPOINT + "/beneList/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BeneCountryDTO>>() {
					});
		} catch (Exception e) {
			log.debug("Bene country list ", e);
		}
		return response.getBody();
	}

	
	public ApiResponse<BeneCountryDTO> getBeneficiaryCountryList(BigDecimal beneCountryId) {
		ResponseEntity<ApiResponse<BeneCountryDTO>> response = null;
		try {
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal customerId = jaxMetaInfo.getCustomerId();
			String userType       = jaxMetaInfo.getChannel().toString();
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			StringBuffer sb = new StringBuffer();
			sb.append("?customerId=").append(customerId).append("&userType=").append(userType).append("&countryId=")
					.append(countryId).append("&beneCountryId=").append(beneCountryId);
			log.info("Bene Clinet to get bene list Input String :" + sb.toString());
			String url = baseUrl.toString() + BENE_API_ENDPOINT + "/benecountry/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BeneCountryDTO>>() {
					});
		} catch (Exception e) {
			log.debug("Bene country list ", e);
		}
		return response.getBody();
	}

	public ApiResponse<AccountTypeDto> getBeneficiaryAccountType(BigDecimal beneCountryId) {
		ResponseEntity<ApiResponse<AccountTypeDto>> response = null;
		try {

			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			StringBuffer sb = new StringBuffer();
			sb.append("?beneCountryId=").append(beneCountryId);
			log.info("Bene Clinet to get bene list Input String :" + sb.toString());
			String url = baseUrl.toString() + BENE_API_ENDPOINT + "/accounttype/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<AccountTypeDto>>() {
					});
		} catch (Exception e) {
			log.debug("Bene country list ", e);
		}
		return response.getBody();
	}

}
