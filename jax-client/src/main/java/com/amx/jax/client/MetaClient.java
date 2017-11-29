package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.META_API_ENDPOINT;

import org.apache.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.amx.amxlib.meta.model.ApplicationSetupDTO;
import com.amx.amxlib.meta.model.CountryMasterDTO;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.response.ApiResponse;

/**
 * 
 * @author :Rabil
 * Date    :23/11/2017 
 *
 */

public class MetaClient extends AbstractJaxServiceClient{
	private Logger log = Logger.getLogger(MetaClient.class);


	public ResponseEntity<ApiResponse<ApplicationSetupDTO>> applicationCountry() {
		ResponseEntity<ApiResponse<ApplicationSetupDTO>> response = null;
		try {
			log.info("Get all the applciation country ");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/applcountry/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<ApplicationSetupDTO>>(){});
			
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	
	
	public ResponseEntity<ApiResponse<ApplicationSetupDTO>> applicationCountryByCountryAndCompany(String countryId,String companyId) {
		ResponseEntity<ApiResponse<ApplicationSetupDTO>> response = null;
		try {
			log.info("Get all the applciation country ");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/applcountry/"+countryId+"/"+companyId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<ApplicationSetupDTO>>(){});
			
			
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	//CountryMasterDTO
	
	public ResponseEntity<ApiResponse<CountryMasterDTO>> getClientForAllCountry() {
		ResponseEntity<ApiResponse<CountryMasterDTO>> response = null;
		try {
			log.info("Get all the applciation country ");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/country/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<CountryMasterDTO>>(){});
			
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	
	public ResponseEntity<ApiResponse<CountryMasterDTO>> getClientForAllCountryByLanguageId(String languageId) {
		ResponseEntity<ApiResponse<CountryMasterDTO>> response = null;
		try {
			log.info("Get all the applciation country "+languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/country/"+languageId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<CountryMasterDTO>>(){});
			
			
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	public ResponseEntity<ApiResponse<CountryMasterDTO>> getClientForAllCountryByLanguageId(String languageId,String countryId) {
		ResponseEntity<ApiResponse<CountryMasterDTO>> response = null;
		try {
			log.info("Get all the applciation country "+languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/country/"+languageId+"/"+countryId;
			
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<CountryMasterDTO>>(){});
		
			
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	
	public ResponseEntity<ApiResponse<CountryMasterDTO>> getClientForBusinessCountry(String languageId) {
		ResponseEntity<ApiResponse<CountryMasterDTO>> response = null;
		try {
			log.info("Get all the applciation country "+languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/country/bc/"+languageId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<CountryMasterDTO>>(){});
		
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	
	public ResponseEntity<ApiResponse<QuestModelDTO>> getClientForSequrityQuestion(String languageId,String countryId) {
		ResponseEntity<ApiResponse<QuestModelDTO>> response = null;
		try {
			log.info("Get all the applciation country "+languageId+"\t countryId :"+countryId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/quest/"+languageId+"/"+countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<QuestModelDTO>>(){});
		
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	public ResponseEntity<ApiResponse<QuestModelDTO>> getClientForSequrityQuestionById(String languageId,String countryId,String questionId) {
		ResponseEntity<ApiResponse<QuestModelDTO>> response = null;
		try {
			log.info("Get all the applciation country "+languageId+"\t countryId :"+countryId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/quest/"+languageId+"/"+countryId+"/"+questionId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<QuestModelDTO>>(){});
		
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	
	
	public ApiResponse getClientForTermsAndCondition(String languageId) {
		ApiResponse response = null;
		try {
			log.info("Terms and Condition "+languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/terms/"+languageId;
			response = restTemplate.getForObject(url, ApiResponse.class);
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	
	public ApiResponse getClientForTermsAndConditionAsPerCountry(String languageId,String countryId) {
		ApiResponse response = null;
		try {
			log.info("Terms and Condition "+languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/terms/"+languageId+"/"+countryId;
			response = restTemplate.getForObject(url, ApiResponse.class);
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	

	public ApiResponse getClienWhyAskInfo(String languageId,String countryId) {
		ApiResponse response = null;
		try {
			log.info("Terms and Condition "+languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/why/"+languageId+"/"+countryId;
			response = restTemplate.getForObject(url, ApiResponse.class);
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	
	public ApiResponse getClienFinancialYear() {
		ApiResponse response = null;
		try {
			log.info("Financial Year");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/fyear/";
			response = restTemplate.getForObject(url, ApiResponse.class);
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	
	public ApiResponse getClienContactUsTime() {
		ApiResponse response = null;
		try {
			log.info("Contact Us time");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/helpdtime/";
			response = restTemplate.getForObject(url, ApiResponse.class);
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	public ApiResponse getClienHelpDeskNo() {
		ApiResponse response = null;
		try {
			log.info("Contact Us time");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/helpdno/";
			response = restTemplate.getForObject(url, ApiResponse.class);
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	
	
}
