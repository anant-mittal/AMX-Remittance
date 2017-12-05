package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.META_API_ENDPOINT;

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

import com.amx.amxlib.meta.model.ApplicationSetupDTO;
import com.amx.amxlib.meta.model.AuthenticationLimitCheckDTO;
import com.amx.amxlib.meta.model.CountryMasterDTO;
import com.amx.amxlib.meta.model.MultiCountryDTO;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.meta.model.TermsAndConditionDTO;
import com.amx.amxlib.meta.model.UserFinancialYearDTO;
import com.amx.amxlib.meta.model.WhyDoAskInformationDTO;
import com.amx.amxlib.model.response.ApiResponse;

/**
 * 
 * @author :Rabil
 * Date    :23/11/2017 
 *
 */

@Component
public class MetaClient extends AbstractJaxServiceClient{
	private Logger log = Logger.getLogger(MetaClient.class);
	
	
	@Autowired
	private JaxMetaInfo jaxMetaInfo;


	public ApiResponse<ApplicationSetupDTO> getApplicationCountry() {
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
		return response.getBody();
	}
	
	
	
	public ApiResponse<ApplicationSetupDTO> getApplicationCountryByCountryAndCompany() {
		ResponseEntity<ApiResponse<ApplicationSetupDTO>> response = null;
		try {

			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal companyId = jaxMetaInfo.getCompanyId();
			log.info("Get all the applciation country ");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/applcountry/"+countryId+"/"+companyId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<ApplicationSetupDTO>>(){});
			
			
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response.getBody();
	}
	
	//CountryMasterDTO
	
	public ApiResponse<CountryMasterDTO> getAllCountry() {
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
		return response.getBody();
	}
	
	
	public ApiResponse<CountryMasterDTO> getAllCountryByLanguageId(String languageId) {
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
		return response.getBody();
	}
	
	public ApiResponse<CountryMasterDTO> getAllCountryByLanguageId(String languageId,String countryId) {
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
		return response.getBody();
	}
	
	
	public ApiResponse<CountryMasterDTO> getBusinessCountry(String languageId) {
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
		return response.getBody();
	}
	
	
	public ApiResponse<QuestModelDTO> getSequrityQuestion(String languageId) {
		ResponseEntity<ApiResponse<QuestModelDTO>> response = null;
		try {
			
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			
			
			log.info("Get all the applciation country "+languageId+"\t countryId :"+countryId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/quest/"+languageId+"/"+countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<QuestModelDTO>>(){});
		
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response.getBody();
	}
	
	public ApiResponse<QuestModelDTO> getSequrityQuestionById(String languageId,String questionId) {
		ResponseEntity<ApiResponse<QuestModelDTO>> response = null;
		try {
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			log.info("Get all the applciation country "+languageId+"\t countryId :"+countryId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/quest/"+languageId+"/"+countryId+"/"+questionId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<QuestModelDTO>>(){});
		
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response.getBody();
	}
	
	

	public ApiResponse<TermsAndConditionDTO> getTermsAndCondition(String languageId) {
		ResponseEntity<ApiResponse<TermsAndConditionDTO>> response = null;
		try {
			log.info("Terms and Condition "+languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/terms/"+languageId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<TermsAndConditionDTO>>(){});
		
			
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response.getBody();
	}
	
	
	public ApiResponse<TermsAndConditionDTO> getTermsAndConditionAsPerCountry(String languageId) {
		ResponseEntity<ApiResponse<TermsAndConditionDTO>> response = null;
		try {
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			log.info("Terms and Condition "+languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/terms/"+languageId+"/"+countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<TermsAndConditionDTO>>(){});
		
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response.getBody();
	}
	
	

	public ApiResponse<WhyDoAskInformationDTO> getWhyAskInfo(String languageId) {
		ResponseEntity<ApiResponse<WhyDoAskInformationDTO>> response = null;
		try {
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			log.info("Terms and Condition "+languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/why/"+languageId+"/"+countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<WhyDoAskInformationDTO>>(){});
		
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response.getBody();
	}
	

	
	public ApiResponse<UserFinancialYearDTO> getFinancialYear() {
		ResponseEntity<ApiResponse<UserFinancialYearDTO>> response = null;
		try {
			log.info("Financial Year");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/fyear/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<UserFinancialYearDTO>>(){});
		
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response.getBody();
	}
	
	
	public ApiResponse<AuthenticationLimitCheckDTO> getContactUsTime() {
		ResponseEntity<ApiResponse<AuthenticationLimitCheckDTO>> response = null;
		try {
			log.info("Contact Us time");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/helpdtime/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<AuthenticationLimitCheckDTO>>(){});
		
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response.getBody();
	}
	
	public ApiResponse<AuthenticationLimitCheckDTO> getHelpDeskNo() {
		ResponseEntity<ApiResponse<AuthenticationLimitCheckDTO>> response = null;
		try {
			log.info("Contact Us time");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/helpdno/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<AuthenticationLimitCheckDTO>>(){});
			
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response.getBody();
	}
	
	
	

	
	public ApiResponse<MultiCountryDTO> getMultiCountryList() {
		ResponseEntity<ApiResponse<MultiCountryDTO>> response = null;
		try {
			log.info("Contact Us time");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url =baseUrl.toString()+ META_API_ENDPOINT+"/multi/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<MultiCountryDTO>>(){});
			
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response.getBody();
	}
	
}
