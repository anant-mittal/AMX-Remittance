package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.META_API_ENDPOINT;

import org.apache.log4j.Logger;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.amx.amxlib.model.response.ApiResponse;

/**
 * 
 * @author :Rabil
 * Date    :23/11/2017 
 *
 */

public class MetaClient extends AbstractJaxServiceClient{
	private Logger log = Logger.getLogger(MetaClient.class);


	public ApiResponse applicationCountry() {
		ApiResponse response = null;
		try {
			log.info("Get all the applciation country ");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			//headers.add("meta-info", "{\"country-id\":91}");
			//HttpEntity<AbstractUserModel> entity = new HttpEntity<AbstractUserModel>(model, headers);
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/applcountry/";
			response = restTemplate.getForObject(url, ApiResponse.class);
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	
	
	public ApiResponse applicationCountryByCountryAndCompany(String countryId,String companyId) {
		ApiResponse response = null;
		try {
			log.info("Get all the applciation country ");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/applcountry/"+countryId+"/"+companyId;
			response = restTemplate.getForObject(url, ApiResponse.class);
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	
	
	public ApiResponse getClientForAllCountry() {
		ApiResponse response = null;
		try {
			log.info("Get all the applciation country ");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/country/";
			response = restTemplate.getForObject(url, ApiResponse.class);
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	
	public ApiResponse getClientForAllCountryByLanguageId(String languageId) {
		ApiResponse response = null;
		try {
			log.info("Get all the applciation country "+languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/country/"+languageId;
			response = restTemplate.getForObject(url, ApiResponse.class);
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	public ApiResponse getClientForAllCountryByLanguageId(String languageId,String countryId) {
		ApiResponse response = null;
		try {
			log.info("Get all the applciation country "+languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/country/"+languageId+"/"+countryId;
			response = restTemplate.getForObject(url, ApiResponse.class);
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	
	public ApiResponse getClientForBusinessCountry(String languageId) {
		ApiResponse response = null;
		try {
			log.info("Get all the applciation country "+languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/country/bc/"+languageId;
			response = restTemplate.getForObject(url, ApiResponse.class);
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	public ApiResponse getClientForSequrityQuestion(String languageId,String countryId) {
		ApiResponse response = null;
		try {
			log.info("Get all the applciation country "+languageId+"\t countryId :"+countryId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/quest/"+languageId+"/"+countryId;
			response = restTemplate.getForObject(url, ApiResponse.class);
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response;
	}
	
	public ApiResponse getClientForSequrityQuestionById(String languageId,String countryId,String questionId) {
		ApiResponse response = null;
		try {
			log.info("Get all the applciation country "+languageId+"\t countryId :"+countryId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/quest/"+languageId+"/"+countryId+"/"+questionId;
			response = restTemplate.getForObject(url, ApiResponse.class);
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
	
}
