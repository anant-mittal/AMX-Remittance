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
import org.springframework.web.util.UriComponentsBuilder;

import com.amx.amxlib.exception.AbstractException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.meta.model.ApplicationSetupDTO;
import com.amx.amxlib.meta.model.AuthenticationLimitCheckDTO;
import com.amx.amxlib.meta.model.BankBranchDto;
import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.meta.model.CountryMasterDTO;
import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.meta.model.JaxMetaParameter;
import com.amx.amxlib.meta.model.MultiCountryDTO;
import com.amx.amxlib.meta.model.PrefixDTO;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.meta.model.ServiceGroupMasterDescDto;
import com.amx.amxlib.meta.model.TermsAndConditionDTO;
import com.amx.amxlib.meta.model.UserFinancialYearDTO;
import com.amx.amxlib.meta.model.ViewCityDto;
import com.amx.amxlib.meta.model.ViewDistrictDto;
import com.amx.amxlib.meta.model.ViewStateDto;
import com.amx.amxlib.meta.model.WhyDoAskInformationDTO;
import com.amx.amxlib.model.OnlineConfigurationDto;
import com.amx.amxlib.model.request.GetBankBranchRequest;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.rest.RestService;

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

	@Autowired
	RestService restService;

	public ApiResponse<ApplicationSetupDTO> getApplicationCountry() {
		ResponseEntity<ApiResponse<ApplicationSetupDTO>> response;
		try {
			LOGGER.info("Get all the applciation country ");

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/applcountry/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ApplicationSetupDTO>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getApplicationCountry : ", e);
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

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/applcountry/" + countryId + "/" + companyId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ApplicationSetupDTO>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getApplicationCountryByCountryAndCompany : ", e);
			throw new JaxSystemError();
		} // end of try-catch
		return response.getBody();
	}

	// CountryMasterDTO

	public ApiResponse<CountryMasterDTO> getAllCountry() {
		ResponseEntity<ApiResponse<CountryMasterDTO>> response;
		try {
			LOGGER.info("Get all the applciation country ");

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/country/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CountryMasterDTO>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getAllCountry : ", e);
			throw new JaxSystemError();
		} // end of try-catch
		return response.getBody();
	}

	@Deprecated
	public ApiResponse<CountryMasterDTO> getAllCountryByLanguageId(String languageId) {
		ResponseEntity<ApiResponse<CountryMasterDTO>> response;
		try {
			LOGGER.info("Get all the applciation country " + languageId);

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/country/" + languageId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CountryMasterDTO>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getAllCountryByLanguageId : ", e);
			throw new JaxSystemError();
		} // end of try-catch
		return response.getBody();
	}

	public ApiResponse<CountryMasterDTO> getAllCountryByLanguageId(String languageId, String countryId) {
		ResponseEntity<ApiResponse<CountryMasterDTO>> response;
		try {
			LOGGER.info("Get all the applciation country " + languageId);

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/country/" + languageId + "/" + countryId;

			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CountryMasterDTO>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getAllCountryByLanguageId : ", e);
			throw new JaxSystemError();
		} // end of try-catch
		return response.getBody();
	}

	public ApiResponse<CountryMasterDTO> getBusinessCountry() {
		ResponseEntity<ApiResponse<CountryMasterDTO>> response;
		try {
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			LOGGER.info("Get all the applciation country " + languageId);

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/country/bc/" + languageId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CountryMasterDTO>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getBusinessCountry : ", e);
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

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/quest/" + languageId + "/" + countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<QuestModelDTO>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getSequrityQuestion : ", e);
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

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/quest/" + languageId + "/" + countryId + "/"
					+ questionId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<QuestModelDTO>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getSequrityQuestionById : ", e);
			throw new JaxSystemError();
		} // end of try-catch
		return response.getBody();
	}

	/**
	 * Gives terms and conditions based on metadata like lang id etc
	 * */
	public ApiResponse<TermsAndConditionDTO> getTermsAndCondition() {
		ResponseEntity<ApiResponse<TermsAndConditionDTO>> response;
		try {
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (BigDecimal.ZERO.equals(languageId)) {
				languageId = new BigDecimal(1);
			}
			LOGGER.info("Terms and Condition " + languageId);

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/terms/" + languageId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<TermsAndConditionDTO>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getTermsAndCondition : ", e);
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

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/terms/" + languageId + "/" + countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<TermsAndConditionDTO>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getTermsAndConditionAsPerCountry : ", e);
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

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/why/" + languageId + "/" + countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<WhyDoAskInformationDTO>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getWhyAskInfo : ", e);
			throw new JaxSystemError();
		} // end of try-catch
		return response.getBody();
	}

	public ApiResponse<UserFinancialYearDTO> getFinancialYear() {
		ResponseEntity<ApiResponse<UserFinancialYearDTO>> response;
		try {
			LOGGER.info("Financial Year");

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/fyear/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<UserFinancialYearDTO>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getFinancialYear : ", e);
			throw new JaxSystemError();
		} // end of try-catch
		return response.getBody();
	}

	public ApiResponse<AuthenticationLimitCheckDTO> getContactUsTime() {
		ResponseEntity<ApiResponse<AuthenticationLimitCheckDTO>> response;
		try {
			LOGGER.info("Contact Us time");

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/helpdtime/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<AuthenticationLimitCheckDTO>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getContactUsTime : ", e);
			throw new JaxSystemError();
		} // end of try-catch
		return response.getBody();
	}

	public ApiResponse<AuthenticationLimitCheckDTO> getHelpDeskNo() {
		ResponseEntity<ApiResponse<AuthenticationLimitCheckDTO>> response;
		try {
			LOGGER.info("Contact Us time");

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/helpdno/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<AuthenticationLimitCheckDTO>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getHelpDeskNo : ", e);
			throw new JaxSystemError();
		} // end of try-catch
		return response.getBody();
	}

	public ApiResponse<MultiCountryDTO> getMultiCountryList() {
		ResponseEntity<ApiResponse<MultiCountryDTO>> response;
		try {
			LOGGER.info("Contact Us time");

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/multicountry/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<MultiCountryDTO>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getMultiCountryList : ", e);
			throw new JaxSystemError();
		} // end of try-catch
		return response.getBody();
	}
	
	
	/**
	 * @param beneficiaryCountryId - Beneficiary Country Id
	 * @return List of currency master for passed beneficiary currency
	 * */
	public ApiResponse<CurrencyMasterDTO> getBeneficiaryCurrency(BigDecimal beneficiaryCountryId) {
		ResponseEntity<ApiResponse<CurrencyMasterDTO>> response;
		try {
			LOGGER.info("in getBeneficiaryCurrency");
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/currency/beneservice/";
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("beneficiaryCountryId", beneficiaryCountryId);
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CurrencyMasterDTO>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getBeneficiaryCurrency : ", e);
			throw new JaxSystemError();
		} // end of try-catch
		return response.getBody();
	}
	
	public ApiResponse<CurrencyMasterDTO> getAllOnlineCurrency() {
		ResponseEntity<ApiResponse<CurrencyMasterDTO>> response;
		try {
			LOGGER.info("in getAllOnlineCurrency");
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/currency/online/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CurrencyMasterDTO>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getAllOnlineCurrency : ", e);
			throw new JaxSystemError();
		} // end of try-catch
		return response.getBody();
	}
	
	// added by chetan 30/04/2018 list the country for currency.
	public ApiResponse<CurrencyMasterDTO> getAllExchangeRateCurrencyList() {
		ResponseEntity<ApiResponse<CurrencyMasterDTO>> response;
		try {
			LOGGER.info("in getAllExchangeRateCurrencyList");
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/exchange-rate-currency/list/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CurrencyMasterDTO>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getAllExchangeRateCurrencyList : ", e);
			throw new JaxSystemError();
		} // end of try-catch
		return response.getBody();
	}
	
	

	public ApiResponse<CurrencyMasterDTO> getCurrencyByCountryId(BigDecimal countryId) {
		ResponseEntity<ApiResponse<CurrencyMasterDTO>> response;
		try {
			LOGGER.info("in getAllOnlineCurrency");
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/currency/bycountry/" + countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CurrencyMasterDTO>>() {
					});
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
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

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/districtdesc/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ViewDistrictDto>>() {
					});
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getDistricDesc : ", e);
			throw new JaxSystemError();
		} // end of try-catch
		return response.getBody();
	}

	/**
	 * <p>
	 * Gives the list of banks by country
	 * </p>
	 * 
	 * @param countryId
	 *            - Id value of country
	 */
	public ApiResponse<BankMasterDTO> getBankListForCountry(BigDecimal countryId) {
		ResponseEntity<ApiResponse<BankMasterDTO>> response;
		try {
			LOGGER.info("in getBankListForCountry");

			String endpoint = META_API_ENDPOINT + BANK_MASTER_BY_COUNTRY_API_ENDPOINT;
			endpoint = endpoint.replaceAll("\\{country\\-id\\}", countryId.toPlainString());
			String url = this.getBaseUrl() + endpoint;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BankMasterDTO>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getBankListForCountry : ", e);
			throw new JaxSystemError();
		} // end of try-catch
		return response.getBody();
	}

	public ApiResponse<ViewDistrictDto> getDistrictList(BigDecimal stateId) {
		ResponseEntity<ApiResponse<ViewDistrictDto>> response;
		try {
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (BigDecimal.ZERO.equals(languageId)) {
				languageId = new BigDecimal(1);
			}
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/districtlist/" + languageId +"/"+stateId+"/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ViewDistrictDto>>() {
					});
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getDistrictList : ", e);
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
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/statelist/"+ languageId +"/"+ countryId +"/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ViewStateDto>>() {
					});
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getStateList : ", e);
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

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/statedesc/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ViewStateDto>>() {
					});
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getStateDesc : ", e);
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

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/citylist/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ViewCityDto>>() {
					});
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getCitytList : ", e);
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

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/citydesc/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ViewCityDto>>() {
					});
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getCitytDesc : ", e);
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
			LOGGER.error("exception in getOnlineConfig : ", e);
			throw new JaxSystemError();
		} // end of try-catch
		return response.getBody();
	}

	/**
	 * <p>
	 * Gives the list of bank branches based on parameters like ifsc, swift, name
	 * etc
	 * </p>
	 * 
	 * @param object
	 *            of type {@code GetBankBranchRequest}
	 */
	public ApiResponse<BankBranchDto> getBankBranchList(GetBankBranchRequest request) {
		ResponseEntity<ApiResponse<BankBranchDto>> response;
		try {

			LOGGER.info("In getBankBranchList :");
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/bankbranch/get/";
			HttpEntity<GetBankBranchRequest> requestEntity = new HttpEntity<GetBankBranchRequest>(request, getHeader());
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BankBranchDto>>() {
					});
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getBankBranchList : ", e);
			throw new JaxSystemError();
		} // end of try-catc
		return response.getBody();
	}
	
	/**
	 * <p>
	 * Gives the list of available service groups like bank, cash ,dd etc
	 * etc
	 * </p>
	 * 
	 */
	public ApiResponse<ServiceGroupMasterDescDto> getServiceGroupList() {
		ResponseEntity<ApiResponse<ServiceGroupMasterDescDto>> response;
		try {

			LOGGER.info("In getServiceGroupList :");
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/service-group/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ServiceGroupMasterDescDto>>() {
					});
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getServiceGroupList : ", e);
			throw new JaxSystemError();
		} // end of try-catc
		return response.getBody();
	}
	

	/**
	 * <p>
	 * Gives jax meta parameters like new newBeneTransactionTimeLimit 
	 * </p>
	 * 
	 */
	public ApiResponse<JaxMetaParameter> getJaxMetaParameter() {
		ResponseEntity<ApiResponse<JaxMetaParameter>> response;
		try {
			LOGGER.info("In getJaxMetaParameter :");
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/meta-parameter/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<JaxMetaParameter>>() {
					});
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getJaxMetaParameter : ", e);
			throw new JaxSystemError();
		} // end of try-catc
		return response.getBody();
	}
	
	/**
	 * 
	 * @return To fetch list of PrefixDTO use getResults method of ApiResponse
	 * 
	 */

	public ApiResponse<PrefixDTO> getAllPrefix() {
		ResponseEntity<ApiResponse<PrefixDTO>> response;
		try {
			LOGGER.info("Get all the Prefix ");

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/prefix/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<PrefixDTO>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getAllPrefix : ", e);
			throw new JaxSystemError();
		} // end of try-catch
		return response.getBody();
	}
}
