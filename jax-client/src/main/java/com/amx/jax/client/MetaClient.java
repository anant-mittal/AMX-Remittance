package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.BANK_MASTER_BY_COUNTRY_API_ENDPOINT;
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
import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.meta.model.CountryMasterDTO;
import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.meta.model.MultiCountryDTO;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.meta.model.TermsAndConditionDTO;
import com.amx.amxlib.meta.model.UserFinancialYearDTO;
import com.amx.amxlib.meta.model.ViewCityDto;
import com.amx.amxlib.meta.model.ViewDistrictDto;
import com.amx.amxlib.meta.model.ViewStateDto;
import com.amx.amxlib.meta.model.WhyDoAskInformationDTO;
import com.amx.amxlib.model.OnlineConfigurationDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.amxlib.model.JaxMetaInfo;

/**
 * 
 * @author :Rabil Date :23/11/2017
 *
 */

@Component
public class MetaClient extends AbstractJaxServiceClient {
	private Logger log = Logger.getLogger(MetaClient.class);

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	public ApiResponse<ApplicationSetupDTO> getApplicationCountry() {
		ResponseEntity<ApiResponse<ApplicationSetupDTO>> response = null;
		try {
			log.info("Get all the applciation country ");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/applcountry/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ApplicationSetupDTO>>() {
					});

		} catch (Exception e) {
			log.error("exception in registeruser ", e);
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
			String url = baseUrl.toString() + META_API_ENDPOINT + "/applcountry/" + countryId + "/" + companyId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ApplicationSetupDTO>>() {
					});

		} catch (Exception e) {
			log.error("exception in registeruser ", e);
		}
		return response.getBody();
	}

	// CountryMasterDTO

	public ApiResponse<CountryMasterDTO> getAllCountry() {
		ResponseEntity<ApiResponse<CountryMasterDTO>> response = null;
		try {
			log.info("Get all the applciation country ");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/country/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CountryMasterDTO>>() {
					});

		} catch (Exception e) {
			log.error("exception in registeruser ", e);
		}
		return response.getBody();
	}

	public ApiResponse<CountryMasterDTO> getAllCountryByLanguageId(String languageId) {
		ResponseEntity<ApiResponse<CountryMasterDTO>> response = null;
		try {
			log.info("Get all the applciation country " + languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/country/" + languageId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CountryMasterDTO>>() {
					});

		} catch (Exception e) {
			log.error("exception in registeruser ", e);
		}
		return response.getBody();
	}

	public ApiResponse<CountryMasterDTO> getAllCountryByLanguageId(String languageId, String countryId) {
		ResponseEntity<ApiResponse<CountryMasterDTO>> response = null;
		try {
			log.info("Get all the applciation country " + languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/country/" + languageId + "/" + countryId;

			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CountryMasterDTO>>() {
					});

		} catch (Exception e) {
			log.error("exception in registeruser ", e);
		}
		return response.getBody();
	}

	public ApiResponse<CountryMasterDTO> getBusinessCountry() {
		ResponseEntity<ApiResponse<CountryMasterDTO>> response = null;
		try {
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			log.info("Get all the applciation country " + languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/country/bc/" + languageId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CountryMasterDTO>>() {
					});

		} catch (Exception e) {
			log.error("exception in registeruser ", e);
		}
		return response.getBody();
	}

	public ApiResponse<QuestModelDTO> getSequrityQuestion() {
		ResponseEntity<ApiResponse<QuestModelDTO>> response = null;
		try {

			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (languageId == null && languageId.compareTo(BigDecimal.ZERO) == 0) {
				languageId = new BigDecimal(1);
			}

			log.info("Get all the applciation country " + languageId + "\t countryId :" + countryId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/quest/" + languageId + "/" + countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<QuestModelDTO>>() {
					});

		} catch (Exception e) {
			log.error("exception in registeruser ", e);
		}
		return response.getBody();
	}

	public ApiResponse<QuestModelDTO> getSequrityQuestionById(String questionId) {
		ResponseEntity<ApiResponse<QuestModelDTO>> response = null;
		try {
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (languageId == null && languageId.compareTo(BigDecimal.ZERO) == 0) {
				languageId = new BigDecimal(1);
			}
			log.info("Get all the applciation country " + languageId + "\t countryId :" + countryId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/quest/" + languageId + "/" + countryId + "/"
					+ questionId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<QuestModelDTO>>() {
					});

		} catch (Exception e) {
			log.error("exception in registeruser ", e);
		}
		return response.getBody();
	}

	public ApiResponse<TermsAndConditionDTO> getTermsAndCondition() {
		ResponseEntity<ApiResponse<TermsAndConditionDTO>> response = null;
		try {
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (languageId == null && languageId.compareTo(BigDecimal.ZERO) == 0) {
				languageId = new BigDecimal(1);
			}
			log.info("Terms and Condition " + languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/terms/" + languageId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<TermsAndConditionDTO>>() {
					});

		} catch (Exception e) {
			log.error("exception in registeruser ", e);
		}
		return response.getBody();
	}

	public ApiResponse<TermsAndConditionDTO> getTermsAndConditionAsPerCountry() {
		ResponseEntity<ApiResponse<TermsAndConditionDTO>> response = null;
		try {
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (languageId == null && languageId.compareTo(BigDecimal.ZERO) == 0) {
				languageId = new BigDecimal(1);
			}
			log.info("Terms and Condition " + languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/terms/" + languageId + "/" + countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<TermsAndConditionDTO>>() {
					});

		} catch (Exception e) {
			log.error("exception in registeruser ", e);
		}
		return response.getBody();
	}

	public ApiResponse<WhyDoAskInformationDTO> getWhyAskInfo() {
		ResponseEntity<ApiResponse<WhyDoAskInformationDTO>> response = null;
		try {
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (languageId == null && languageId.compareTo(BigDecimal.ZERO) == 0) {
				languageId = new BigDecimal(1);
			}
			log.info("Terms and Condition " + languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/why/" + languageId + "/" + countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<WhyDoAskInformationDTO>>() {
					});

		} catch (Exception e) {
			log.error("exception in registeruser ", e);
		}
		return response.getBody();
	}

	public ApiResponse<UserFinancialYearDTO> getFinancialYear() {
		ResponseEntity<ApiResponse<UserFinancialYearDTO>> response = null;
		try {
			log.info("Financial Year");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/fyear/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<UserFinancialYearDTO>>() {
					});

		} catch (Exception e) {
			log.error("exception in registeruser ", e);
		}
		return response.getBody();
	}

	public ApiResponse<AuthenticationLimitCheckDTO> getContactUsTime() {
		ResponseEntity<ApiResponse<AuthenticationLimitCheckDTO>> response = null;
		try {
			log.info("Contact Us time");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/helpdtime/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<AuthenticationLimitCheckDTO>>() {
					});

		} catch (Exception e) {
			log.error("exception in registeruser ", e);
		}
		return response.getBody();
	}

	public ApiResponse<AuthenticationLimitCheckDTO> getHelpDeskNo() {
		ResponseEntity<ApiResponse<AuthenticationLimitCheckDTO>> response = null;
		try {
			log.info("Contact Us time");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/helpdno/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<AuthenticationLimitCheckDTO>>() {
					});

		} catch (Exception e) {
			log.error("exception in registeruser ", e);
		}
		return response.getBody();
	}

	public ApiResponse<MultiCountryDTO> getMultiCountryList() {
		ResponseEntity<ApiResponse<MultiCountryDTO>> response = null;
		try {
			log.info("Contact Us time");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/multicountry/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<MultiCountryDTO>>() {
					});

		} catch (Exception e) {
			log.error("exception in registeruser ", e);
		}
		return response.getBody();
	}

	public ApiResponse<CurrencyMasterDTO> getAllOnlineCurrency() {
		ResponseEntity<ApiResponse<CurrencyMasterDTO>> response = null;
		try {
			log.info("in getAllOnlineCurrency");
			MultiValueMap<String, String> headers = getHeader();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/currency/online/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CurrencyMasterDTO>>() {
					});

		} catch (Exception e) {
			log.error("exception in getAllOnlineCurrency ", e);
		}
		return response.getBody();
	}

	public ApiResponse<CurrencyMasterDTO> getCurrencyByCountryId(BigDecimal countryId) {
		ResponseEntity<ApiResponse<CurrencyMasterDTO>> response = null;
		try {
			log.info("in getAllOnlineCurrency");
			MultiValueMap<String, String> headers = getHeader();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/currency/bycountry/" + countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CurrencyMasterDTO>>() {
					});
		} catch (Exception e) {
			log.error("exception in getAllOnlineCurrency ", e);
		}
		return response.getBody();
	}

	public ApiResponse<ViewDistrictDto> getDistricDesc(BigDecimal stateId,BigDecimal districtId) {
		ResponseEntity<ApiResponse<ViewDistrictDto>> response = null;
		try {
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal companyId = jaxMetaInfo.getCompanyId();
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (languageId == null && languageId.compareTo(BigDecimal.ZERO) == 0) {
				languageId = new BigDecimal(1);
			}
			StringBuffer sb = new StringBuffer();
			sb.append("?languageId=").append(languageId).append("&stateId=").append(stateId).append("&districtId=")
					.append(districtId);
			log.info("District Input :" + sb.toString());
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/districtdesc/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ViewDistrictDto>>() {
					});
		} catch (Exception e) {
			log.error("exception in registeruser ", e);

		}
		return response.getBody();
	}

	public ApiResponse<BankMasterDTO> getBankListForCountry(BigDecimal countryId) {
		ResponseEntity<ApiResponse<BankMasterDTO>> response = null;
		try {
			log.info("in getBankListForCountry");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String endpoint = META_API_ENDPOINT + BANK_MASTER_BY_COUNTRY_API_ENDPOINT;
			endpoint = endpoint.replaceAll("\\{country\\-id\\}", countryId.toPlainString());
			String url = baseUrl.toString() + endpoint;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BankMasterDTO>>() {
					});

		} catch (Exception e) {
			log.error("exception in getBankListForCountry ", e);
		}
		return response.getBody();
	}

	public ApiResponse<ViewDistrictDto> getDistrictList(BigDecimal languageId, BigDecimal stateId) {
		ResponseEntity<ApiResponse<ViewDistrictDto>> response = null;
		try {
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal companyId = jaxMetaInfo.getCompanyId();
			if (languageId == null && languageId.compareTo(BigDecimal.ZERO) == 0) {
				languageId = new BigDecimal(1);
			}
			StringBuffer sb = new StringBuffer();
			sb.append("?languageId=").append(languageId).append("&stateId=").append(stateId);
			log.info("District Input :" + sb.toString());
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/districtlist/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ViewDistrictDto>>() {
					});
		} catch (Exception e) {
			log.error("exception in registeruser ", e);

		}
		return response.getBody();
	}

	public ApiResponse<ViewStateDto> getStateList(BigDecimal countryId) {
		ResponseEntity<ApiResponse<ViewStateDto>> response = null;
		try {

			BigDecimal companyId = jaxMetaInfo.getCompanyId();
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (languageId == null && languageId.compareTo(BigDecimal.ZERO) == 0) {
				languageId = new BigDecimal(1);
			}
			StringBuffer sb = new StringBuffer();
			sb.append("?languageId=").append(languageId).append("&countryId=").append(countryId);
			log.info("State Input :" + sb.toString());
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/statelist/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ViewStateDto>>() {
					});
		} catch (Exception e) {
			log.error("exception in registeruser ", e);
		}
		return response.getBody();
	}

	public ApiResponse<ViewStateDto> getStateDesc(BigDecimal countryId, BigDecimal stateId) {
		ResponseEntity<ApiResponse<ViewStateDto>> response = null;
		try {

			BigDecimal companyId = jaxMetaInfo.getCompanyId();
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (languageId == null && languageId.compareTo(BigDecimal.ZERO) == 0) {
				languageId = new BigDecimal(1);
			}
			StringBuffer sb = new StringBuffer();
			sb.append("?languageId=").append(languageId).append("&countryId=").append(countryId).append("&stateId=")
					.append(stateId);
			log.info("State Input :" + sb.toString());
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/statedesc/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ViewStateDto>>() {
					});
		} catch (Exception e) {
			log.error("exception in registeruser ", e);
		}
		return response.getBody();
	}
	
	
	public ApiResponse<ViewCityDto> getCitytList(BigDecimal districtId) {
		ResponseEntity<ApiResponse<ViewCityDto>> response = null;
		try {
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal companyId = jaxMetaInfo.getCompanyId();
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (languageId == null && languageId.compareTo(BigDecimal.ZERO) == 0) {
				languageId = new BigDecimal(1);
			}
			StringBuffer sb = new StringBuffer();
			sb.append("?languageId=").append(languageId).append("&districtId=").append(districtId);
			log.info("City Input :" + sb.toString());
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/citylist/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<ViewCityDto>>() {});
		} catch (Exception e) {
			log.error("exception in registeruser ", e);

		}
		return response.getBody();
	}
	
	public ApiResponse<ViewCityDto> getCitytDesc(BigDecimal districtId,BigDecimal cityId) {
		ResponseEntity<ApiResponse<ViewCityDto>> response = null;
		try {
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal companyId = jaxMetaInfo.getCompanyId();
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			
			if (languageId == null && languageId.compareTo(BigDecimal.ZERO) == 0) {
				languageId = new BigDecimal(1);
			}
			StringBuffer sb = new StringBuffer();
			sb.append("?languageId=").append(languageId).append("&districtId=").append(districtId).append("&cityId=").append(cityId);
			log.info("City Input :" + sb.toString());
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = baseUrl.toString() + META_API_ENDPOINT + "/citydesc/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<ViewCityDto>>() {});
		} catch (Exception e) {
			log.error("exception in registeruser ", e);

		}
		return response.getBody();
	}
	
	public ApiResponse<OnlineConfigurationDto> getOnlineConfig(String ind) {
		ResponseEntity<ApiResponse<OnlineConfigurationDto>> response = null;
		try {

			log.info("In getOnlineConfig :");
			String url = baseUrl.toString() + META_API_ENDPOINT + "/onlineconfig/" + ind;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<OnlineConfigurationDto>>() {
					});
		} catch (Exception e) {
			log.error("exception in getOnlineConfig ", e);

		}
		return response.getBody();
	}
	

}
