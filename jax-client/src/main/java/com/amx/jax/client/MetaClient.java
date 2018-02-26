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

import com.amx.amxlib.exception.AbstractException;
import com.amx.amxlib.exception.JaxSystemError;
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
	private static final Logger LOGGER = Logger.getLogger(MetaClient.class);

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	public ApiResponse<ApplicationSetupDTO> getApplicationCountry() {
		ResponseEntity<ApiResponse<ApplicationSetupDTO>> response;
		try {
			LOGGER.info("Get all the applciation country ");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/applcountry/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ApplicationSetupDTO>>() {
					});

		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getApplicationCountry : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<ApplicationSetupDTO> getApplicationCountryByCountryAndCompany() {
		ResponseEntity<ApiResponse<ApplicationSetupDTO>> response;
		try {

			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal companyId = jaxMetaInfo.getCompanyId();
			LOGGER.info("Get all the applciation country ");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/applcountry/" + countryId + "/" + companyId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ApplicationSetupDTO>>() {
					});

		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getApplicationCountryByCountryAndCompany : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	// CountryMasterDTO

	public ApiResponse<CountryMasterDTO> getAllCountry() {
		ResponseEntity<ApiResponse<CountryMasterDTO>> response;
		try {
			LOGGER.info("Get all the applciation country ");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/country/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CountryMasterDTO>>() {
					});

		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getAllCountry : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<CountryMasterDTO> getAllCountryByLanguageId(String languageId) {
		ResponseEntity<ApiResponse<CountryMasterDTO>> response;
		try {
			LOGGER.info("Get all the applciation country " + languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/country/" + languageId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CountryMasterDTO>>() {
					});

		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getAllCountryByLanguageId : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<CountryMasterDTO> getAllCountryByLanguageId(String languageId, String countryId) {
		ResponseEntity<ApiResponse<CountryMasterDTO>> response;
		try {
			LOGGER.info("Get all the applciation country " + languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/country/" + languageId + "/" + countryId;

			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CountryMasterDTO>>() {
					});

		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getAllCountryByLanguageId : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<CountryMasterDTO> getBusinessCountry() {
		ResponseEntity<ApiResponse<CountryMasterDTO>> response;
		try {
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			LOGGER.info("Get all the applciation country " + languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/country/bc/" + languageId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CountryMasterDTO>>() {
					});

		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getBusinessCountry : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<QuestModelDTO> getSequrityQuestion() {
		ResponseEntity<ApiResponse<QuestModelDTO>> response;
		try {

			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (BigDecimal.ZERO.equals(languageId)) {
				languageId = new BigDecimal(1);
			}

			LOGGER.info("Get all the applciation country " + languageId + "\t countryId :" + countryId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/quest/" + languageId + "/" + countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<QuestModelDTO>>() {
					});

		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getSequrityQuestion : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<QuestModelDTO> getSequrityQuestionById(String questionId) {
		ResponseEntity<ApiResponse<QuestModelDTO>> response;
		try {
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (BigDecimal.ZERO.equals(languageId)) {
				languageId = new BigDecimal(1);
			}
			LOGGER.info("Get all the applciation country " + languageId + "\t countryId :" + countryId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/quest/" + languageId + "/" + countryId + "/"
					+ questionId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<QuestModelDTO>>() {
					});

		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getSequrityQuestionById : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<TermsAndConditionDTO> getTermsAndCondition() {
		ResponseEntity<ApiResponse<TermsAndConditionDTO>> response;
		try {
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (BigDecimal.ZERO.equals(languageId)) {
				languageId = new BigDecimal(1);
			}
			LOGGER.info("Terms and Condition " + languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/terms/" + languageId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<TermsAndConditionDTO>>() {
					});

		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getTermsAndCondition : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<TermsAndConditionDTO> getTermsAndConditionAsPerCountry() {
		ResponseEntity<ApiResponse<TermsAndConditionDTO>> response;
		try {
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (BigDecimal.ZERO.equals(languageId)) {
				languageId = new BigDecimal(1);
			}
			LOGGER.info("Terms and Condition " + languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/terms/" + languageId + "/" + countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<TermsAndConditionDTO>>() {
					});

		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getTermsAndConditionAsPerCountry : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<WhyDoAskInformationDTO> getWhyAskInfo() {
		ResponseEntity<ApiResponse<WhyDoAskInformationDTO>> response;
		try {
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (BigDecimal.ZERO.equals(languageId)) {
				
				languageId = new BigDecimal(1);
			}
			LOGGER.info("Terms and Condition " + languageId);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/why/" + languageId + "/" + countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<WhyDoAskInformationDTO>>() {
					});

		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getWhyAskInfo : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<UserFinancialYearDTO> getFinancialYear() {
		ResponseEntity<ApiResponse<UserFinancialYearDTO>> response;
		try {
			LOGGER.info("Financial Year");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/fyear/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<UserFinancialYearDTO>>() {
					});

		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getFinancialYear : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<AuthenticationLimitCheckDTO> getContactUsTime() {
		ResponseEntity<ApiResponse<AuthenticationLimitCheckDTO>> response;
		try {
			LOGGER.info("Contact Us time");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/helpdtime/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<AuthenticationLimitCheckDTO>>() {
					});

		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getContactUsTime : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<AuthenticationLimitCheckDTO> getHelpDeskNo() {
		ResponseEntity<ApiResponse<AuthenticationLimitCheckDTO>> response;
		try {
			LOGGER.info("Contact Us time");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/helpdno/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<AuthenticationLimitCheckDTO>>() {
					});

		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getHelpDeskNo : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<MultiCountryDTO> getMultiCountryList() {
		ResponseEntity<ApiResponse<MultiCountryDTO>> response;
		try {
			LOGGER.info("Contact Us time");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/multicountry/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<MultiCountryDTO>>() {
					});

		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getMultiCountryList : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<CurrencyMasterDTO> getAllOnlineCurrency() {
		ResponseEntity<ApiResponse<CurrencyMasterDTO>> response;
		try {
			LOGGER.info("in getAllOnlineCurrency");
			MultiValueMap<String, String> headers = getHeader();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/currency/online/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CurrencyMasterDTO>>() {
					});

		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getAllOnlineCurrency : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<CurrencyMasterDTO> getCurrencyByCountryId(BigDecimal countryId) {
		ResponseEntity<ApiResponse<CurrencyMasterDTO>> response;
		try {
			LOGGER.info("in getAllOnlineCurrency");
			MultiValueMap<String, String> headers = getHeader();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/currency/bycountry/" + countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CurrencyMasterDTO>>() {
					});
		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getCurrencyByCountryId : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<ViewDistrictDto> getDistricDesc(BigDecimal stateId, BigDecimal districtId) {
		ResponseEntity<ApiResponse<ViewDistrictDto>> response;
		try {
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (BigDecimal.ZERO.equals(languageId)) {
				languageId = new BigDecimal(1);
			}
			StringBuffer sb = new StringBuffer();
			sb.append("?languageId=").append(languageId).append("&stateId=").append(stateId).append("&districtId=")
					.append(districtId);
			LOGGER.info("District Input :" + sb.toString());
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/districtdesc/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ViewDistrictDto>>() {
					});
		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getDistricDesc : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<BankMasterDTO> getBankListForCountry(BigDecimal countryId) {
		ResponseEntity<ApiResponse<BankMasterDTO>> response;
		try {
			LOGGER.info("in getBankListForCountry");
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String endpoint = META_API_ENDPOINT + BANK_MASTER_BY_COUNTRY_API_ENDPOINT;
			endpoint = endpoint.replaceAll("\\{country\\-id\\}", countryId.toPlainString());
			String url = this.getBaseUrl() + endpoint;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BankMasterDTO>>() {
					});

		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getBankListForCountry : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<ViewDistrictDto> getDistrictList(BigDecimal languageId, BigDecimal stateId) {
		ResponseEntity<ApiResponse<ViewDistrictDto>> response;
		try {
			if (BigDecimal.ZERO.equals(languageId)) {
				languageId = new BigDecimal(1);
			}
			StringBuffer sb = new StringBuffer();
			sb.append("?languageId=").append(languageId).append("&stateId=").append(stateId);
			LOGGER.info("District Input :" + sb.toString());
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/districtlist/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ViewDistrictDto>>() {
					});
		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getDistrictList : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<ViewStateDto> getStateList(BigDecimal countryId) {
		ResponseEntity<ApiResponse<ViewStateDto>> response;
		try {

			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (BigDecimal.ZERO.equals(languageId)) {
				languageId = new BigDecimal(1);
			}
			StringBuffer sb = new StringBuffer();
			sb.append("?languageId=").append(languageId).append("&countryId=").append(countryId);
			LOGGER.info("State Input :" + sb.toString());
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/statelist/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ViewStateDto>>() {
					});
		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getStateList : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<ViewStateDto> getStateDesc(BigDecimal countryId, BigDecimal stateId) {
		ResponseEntity<ApiResponse<ViewStateDto>> response;
		try {

			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (BigDecimal.ZERO.equals(languageId)) {
				languageId = new BigDecimal(1);
			}
			StringBuffer sb = new StringBuffer();
			sb.append("?languageId=").append(languageId).append("&countryId=").append(countryId).append("&stateId=")
					.append(stateId);
			LOGGER.info("State Input :" + sb.toString());
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/statedesc/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ViewStateDto>>() {
					});
		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getStateDesc : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<ViewCityDto> getCitytList(BigDecimal districtId) {
		ResponseEntity<ApiResponse<ViewCityDto>> response;
		try {
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (BigDecimal.ZERO.equals(languageId)) {
				languageId = new BigDecimal(1);
			}
			StringBuffer sb = new StringBuffer();
			sb.append("?languageId=").append(languageId).append("&districtId=").append(districtId);
			LOGGER.info("City Input :" + sb.toString());
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/citylist/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ViewCityDto>>() {
					});
		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getCitytList : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<ViewCityDto> getCitytDesc(BigDecimal districtId, BigDecimal cityId) {
		ResponseEntity<ApiResponse<ViewCityDto>> response;
		try {
			BigDecimal languageId = jaxMetaInfo.getLanguageId();

			if (BigDecimal.ZERO.equals(languageId)) {
				languageId = new BigDecimal(1);
			}
			StringBuffer sb = new StringBuffer();
			sb.append("?languageId=").append(languageId).append("&districtId=").append(districtId).append("&cityId=")
					.append(cityId);
			LOGGER.info("City Input :" + sb.toString());
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/citydesc/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ViewCityDto>>() {
					});
		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getCitytDesc : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

	public ApiResponse<OnlineConfigurationDto> getOnlineConfig(String ind) {
		ResponseEntity<ApiResponse<OnlineConfigurationDto>> response;
		try {

			LOGGER.info("In getOnlineConfig :");
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/onlineconfig/" + ind + "/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<OnlineConfigurationDto>>() {
					});
		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getOnlineConfig : ",e);
            throw new JaxSystemError();
        } // end of try-catch
		return response.getBody();
	}

}
