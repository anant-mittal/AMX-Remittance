package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.BENE_API_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.REMIT_API_ENDPOINT;

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
import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.amxlib.meta.model.RemittancePageDto;
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
	public ApiResponse<BeneficiaryListDTO> getBeneficiaryList(BigDecimal beneCountryId) {
		ResponseEntity<ApiResponse<BeneficiaryListDTO>> response = null;
		MultiValueMap<String, String> headers = getHeader();
		StringBuffer sb = new StringBuffer();
		sb.append("?beneCountryId=").append(beneCountryId);
		log.info("Bene Clinet to get bene list Input String :" + sb.toString());
		String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/beneList/" + sb.toString();
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
		response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
				});

		return response.getBody();
	}

	public ApiResponse<BeneCountryDTO> getBeneficiaryCountryList(BigDecimal beneCountryId) {
		ResponseEntity<ApiResponse<BeneCountryDTO>> response = null;
		BigDecimal countryId = jaxMetaInfo.getCountryId();
		BigDecimal customerId = jaxMetaInfo.getCustomerId();
		String userType = jaxMetaInfo.getChannel().toString();
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		StringBuffer sb = new StringBuffer();
		sb.append("?beneCountryId=").append(beneCountryId);
		log.info("Bene Clinet to get bene list Input String :" + sb.toString());
		String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/benecountry/" + sb.toString();
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
		response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<ApiResponse<BeneCountryDTO>>() {
				});

		return response.getBody();
	}

	public ApiResponse<RemittancePageDto> defaultBeneficiary(BigDecimal beneRealtionId, BigDecimal transactionId) {
		ResponseEntity<ApiResponse<RemittancePageDto>> response = null;

		log.info("Default Beneficiary");

		StringBuffer sb = new StringBuffer();

		if (beneRealtionId != null || transactionId != null) {
			sb.append("?");
		}

		if (beneRealtionId != null) {
			sb.append("beneRelationId=").append(beneRealtionId).append("&");
		}
		if (transactionId != null) {
			sb.append("transactionId=").append(transactionId);
		}

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
		String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/defaultbene/" + sb.toString();
		response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<ApiResponse<RemittancePageDto>>() {
				});

		return response.getBody();
	}

	public ApiResponse<BeneficiaryListDTO> beneDisable(BigDecimal beneMasSeqId, String remarks) {
		ResponseEntity<ApiResponse<BeneficiaryListDTO>> response = null;

		log.info("Transaction Histroy");

		StringBuffer sb = new StringBuffer();
		sb.append("?beneMasSeqId=").append(beneMasSeqId).append("&remarks=").append(remarks);
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
		String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/disable/" + sb.toString();
		response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
				});

		return response.getBody();
	}

	public ApiResponse<BeneficiaryListDTO> beneUpdate(BeneficiaryListDTO beneficiarydto) {
		ResponseEntity<ApiResponse<BeneficiaryListDTO>> response = null;

		log.info("Bene update Client :" + beneficiarydto.getCustomerId() + "\t customerId :"
				+ beneficiarydto.getBeneficiaryRelationShipSeqId());
		HttpEntity<String> requestEntity = new HttpEntity<String>(util.marshall(beneficiarydto), getHeader());
		String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/beneupdate/";
		response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
				});

		return response.getBody();
	}

	public ApiResponse<AccountTypeDto> getBeneficiaryAccountType(BigDecimal beneCountryId) {
		ResponseEntity<ApiResponse<AccountTypeDto>> response = null;

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		StringBuffer sb = new StringBuffer();
		sb.append("?beneCountryId=").append(beneCountryId);
		log.info("Bene Clinet to get bene list Input String :" + sb.toString());
		String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/accounttype/" + sb.toString();
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
		response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<ApiResponse<AccountTypeDto>>() {
				});

		return response.getBody();
	}

}
