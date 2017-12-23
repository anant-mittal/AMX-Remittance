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
public class BeneClient extends AbstractJaxServiceClient{

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
		try {
			
			MultiValueMap<String, String> headers = getHeader();
			StringBuffer sb = new StringBuffer();
			sb.append("?beneCountryId=").append(beneCountryId);
			log.info("Bene Clinet to get bene list Input String :" + sb.toString());
			String url = baseUrl.toString() + BENE_API_ENDPOINT + "/beneList/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {});
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
			sb.append("?beneCountryId=").append(beneCountryId);
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

	
	
	
	public ApiResponse<RemittancePageDto> defaultBeneficiary(BigDecimal beneRealtionId) {
		ResponseEntity<ApiResponse<RemittancePageDto>> response = null;
		try {
			
			log.info("Default Beneficiary");
			
			StringBuffer sb = new StringBuffer();
			sb.append("?beneRealtionId=").append(beneRealtionId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url =baseUrl.toString()+ BENE_API_ENDPOINT+"/defaultbene/"+sb.toString();
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,new ParameterizedTypeReference<ApiResponse<RemittancePageDto>>(){});
		} catch (Exception e) {
			log.debug("Default Beneficiary bene client ", e);
		}
		return response.getBody();
	}
	
	
	public ApiResponse<BeneficiaryListDTO> beneDisable(BigDecimal beneMasSeqId,String remarks) {
		ResponseEntity<ApiResponse<BeneficiaryListDTO>> response = null;
		try {
			
			log.info("Transaction Histroy");
			
			StringBuffer sb = new StringBuffer();
			sb.append("?beneMasSeqId=").append(beneMasSeqId).append("&remarks=").append(remarks);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url =baseUrl.toString()+ BENE_API_ENDPOINT+"/disable/"+sb.toString();
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>(){});
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response.getBody();
	}
	
	
	

	public ApiResponse<BeneficiaryListDTO> beneUpdate(BeneficiaryListDTO beneficiarydto) {
		ResponseEntity<ApiResponse<BeneficiaryListDTO>> response = null;
		try {
		
		log.info("Bene update Client :"+beneficiarydto.getCustomerId()+"\t customerId :"+beneficiarydto.getBeneficiaryRelationShipSeqId());
		HttpEntity<String> requestEntity = new HttpEntity<String>(util.marshall(beneficiarydto), getHeader());
		String url = baseUrl.toString() + REMIT_API_ENDPOINT+"/beneupdate/";
		response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {});
		}catch(Exception e) {
			log.error("exception in saveSecurityQuestions ", e);
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
