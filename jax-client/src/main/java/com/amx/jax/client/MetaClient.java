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

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.meta.model.ApplicationSetupDTO;
import com.amx.amxlib.meta.model.AuthenticationLimitCheckDTO;
import com.amx.amxlib.meta.model.BankBranchDto;
import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.meta.model.BranchDetailDTO;
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
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.rest.RestMetaRequestOutFilter;
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
	private BeneClient beneClient;

	@Autowired
	RestService restService;

	@Autowired
	RestMetaRequestOutFilter<JaxMetaInfo> metaFilter;

	public AmxApiResponse<ApplicationSetupDTO, Object> getApplicationCountry() {

		try {
			LOGGER.info("Get all the applciation country ");

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/applcountry/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<ApplicationSetupDTO, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getApplicationCountry : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public AmxApiResponse<ApplicationSetupDTO, Object> getApplicationCountryByCountryAndCompany() {
		try {
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal companyId = jaxMetaInfo.getCompanyId();

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/applcountry/" + countryId + "/" + companyId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());

			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<ApplicationSetupDTO, Object>>() {
					});

		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getApplicationCountryByCountryAndCompany : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public AmxApiResponse<CountryMasterDTO, Object> getAllCountry() {
		try {
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/country/";
			return restService.ajax(url).filter(metaFilter).get().asApiResponse(CountryMasterDTO.class);
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getAllCountry : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	@Deprecated
	public AmxApiResponse<CountryMasterDTO, Object> getAllCountryByLanguageId(String languageId) {

		try {
			LOGGER.info("Get all the applciation country " + languageId);

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/country/" + languageId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<CountryMasterDTO, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getAllCountryByLanguageId : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public AmxApiResponse<CountryMasterDTO, Object> getAllCountryByLanguageId(String languageId, String countryId) {

		try {
			LOGGER.info("Get all the applciation country " + languageId);

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/country/" + languageId + "/" + countryId;

			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<CountryMasterDTO, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getAllCountryByLanguageId : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public AmxApiResponse<CountryMasterDTO, Object> getBusinessCountry() {

		try {
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			LOGGER.info("Get all the applciation country " + languageId);

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/country/bc/" + languageId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<CountryMasterDTO, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getBusinessCountry : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public AmxApiResponse<QuestModelDTO, Object> getSequrityQuestion() {

		try {

			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (BigDecimal.ZERO.equals(languageId)) {
				languageId = new BigDecimal(1);
			}

			LOGGER.info("Get all the applciation country " + languageId + "\t countryId :" + countryId);

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/quest/" + languageId + "/" + countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<QuestModelDTO, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getSequrityQuestion : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public AmxApiResponse<QuestModelDTO, Object> getSequrityQuestionById(String questionId) {

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
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<QuestModelDTO, Object>>() {
					});

		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getSequrityQuestionById : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	/**
	 * Gives terms and conditions based on metadata like lang id etc
	 */
	public AmxApiResponse<TermsAndConditionDTO, Object> getTermsAndCondition() {

		try {
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (BigDecimal.ZERO.equals(languageId)) {
				languageId = new BigDecimal(1);
			}
			LOGGER.info("Terms and Condition " + languageId);

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/terms/" + languageId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<TermsAndConditionDTO, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getTermsAndCondition : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public AmxApiResponse<TermsAndConditionDTO, Object> getTermsAndConditionAsPerCountry() {
		try {
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (BigDecimal.ZERO.equals(languageId)) {
				languageId = new BigDecimal(1);
			}
			LOGGER.info("Terms and Condition " + languageId);

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/terms/" + languageId + "/" + countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<TermsAndConditionDTO, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getTermsAndConditionAsPerCountry : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public AmxApiResponse<WhyDoAskInformationDTO, Object> getWhyAskInfo() {
		try {
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (BigDecimal.ZERO.equals(languageId)) {

				languageId = new BigDecimal(1);
			}
			LOGGER.info("Terms and Condition " + languageId);

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/why/" + languageId + "/" + countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<WhyDoAskInformationDTO, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getWhyAskInfo : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public AmxApiResponse<UserFinancialYearDTO, Object> getFinancialYear() {
		try {
			LOGGER.info("Financial Year");

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/fyear/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<UserFinancialYearDTO, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getFinancialYear : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public AmxApiResponse<AuthenticationLimitCheckDTO, Object> getContactUsTime() {
		try {
			LOGGER.info("Contact Us time");

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/helpdtime/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<AuthenticationLimitCheckDTO, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getContactUsTime : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public AmxApiResponse<AuthenticationLimitCheckDTO, Object> getHelpDeskNo() {
		try {
			LOGGER.info("Contact Us time");

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/helpdno/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<AuthenticationLimitCheckDTO, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getHelpDeskNo : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public AmxApiResponse<MultiCountryDTO, Object> getMultiCountryList() {
		try {
			LOGGER.info("Contact Us time");

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/multicountry/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<MultiCountryDTO, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getMultiCountryList : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	/**
	 * @param beneficiaryCountryId
	 * @return AmxApiResponse<CurrencyMasterDTO>
	 */
	public AmxApiResponse<CurrencyMasterDTO, Object> getBeneficiaryCurrency(BigDecimal beneficiaryCountryId) {
		return this.getBeneficiaryCurrency(beneficiaryCountryId, null, null);
	}

	/**
	 * @param beneficiaryCountryId
	 * @param serviceGroupId
	 *            - bank or cash
	 * @param routingBankId
	 *            - service provider id in case of cash or bank id in case of bank
	 * @return CurrencyMasterDTO
	 */
	public AmxApiResponse<CurrencyMasterDTO, Object> getBeneficiaryCurrency(BigDecimal beneficiaryCountryId,
			BigDecimal serviceGroupId, BigDecimal routingBankId) {
		ResponseEntity<AmxApiResponse<CurrencyMasterDTO, Object>> response;
		try {
			LOGGER.info("in getBeneficiaryCurrency");
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/currency/beneservice/";

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
					.queryParam("beneficiaryCountryId", beneficiaryCountryId)
					.queryParam("serviceGroupId", serviceGroupId).queryParam("routingBankId", routingBankId);
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<AmxApiResponse<CurrencyMasterDTO, Object>>() {
					});

		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getBeneficiaryCurrency : ", e);
			throw new JaxSystemError();
		} // end of try-catch
		return response.getBody();
	}

	public AmxApiResponse<CurrencyMasterDTO, Object> getAllOnlineCurrency() {
		try {
			LOGGER.info("in getAllOnlineCurrency");
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/currency/online/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<CurrencyMasterDTO, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getAllOnlineCurrency : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	// added by chetan 30/04/2018 list the country for currency.
	public AmxApiResponse<CurrencyMasterDTO, Object> getAllExchangeRateCurrencyList() {
		try {
			LOGGER.info("in getAllExchangeRateCurrencyList");
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/exchange-rate-currency/list/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<CurrencyMasterDTO, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getAllExchangeRateCurrencyList : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public AmxApiResponse<CurrencyMasterDTO, Object> getCurrencyByCountryId(BigDecimal countryId) {
		try {
			LOGGER.info("in getAllOnlineCurrency");
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/currency/bycountry/" + countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<CurrencyMasterDTO, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public AmxApiResponse<ViewDistrictDto, Object> getDistricDesc(BigDecimal stateId, BigDecimal districtId) {
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
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<ViewDistrictDto, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getDistricDesc : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	/**
	 * <p>
	 * Gives the list of banks by country
	 * </p>
	 * 
	 * @param countryId
	 *            - Id value of country
	 */
	public AmxApiResponse<BankMasterDTO, Object> getBankListForCountry(BigDecimal countryId) {
		try {
			LOGGER.info("in getBankListForCountry");

			String endpoint = META_API_ENDPOINT + BANK_MASTER_BY_COUNTRY_API_ENDPOINT;
			endpoint = endpoint.replaceAll("\\{country\\-id\\}", countryId.toPlainString());
			String url = this.getBaseUrl() + endpoint;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<BankMasterDTO, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getBankListForCountry : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public AmxApiResponse<ViewDistrictDto, Object> getDistrictList(BigDecimal stateId) {
		try {
			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (BigDecimal.ZERO.equals(languageId)) {
				languageId = new BigDecimal(1);
			}
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/districtlist/" + languageId + "/" + stateId + "/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<ViewDistrictDto, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getDistrictList : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public AmxApiResponse<ViewStateDto, Object> getStateList(BigDecimal countryId) {
		try {

			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (BigDecimal.ZERO.equals(languageId)) {
				languageId = new BigDecimal(1);
			}
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/statelist/" + languageId + "/" + countryId + "/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<ViewStateDto, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getStateList : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public AmxApiResponse<ViewStateDto, Object> getStateDesc(BigDecimal countryId, BigDecimal stateId) {
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
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<ViewStateDto, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getStateDesc : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public AmxApiResponse<ViewCityDto, Object> getCitytList(BigDecimal districtId) {
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
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<ViewCityDto, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getCitytList : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public AmxApiResponse<ViewCityDto, Object> getCitytDesc(BigDecimal districtId, BigDecimal cityId) {
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
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<ViewCityDto, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getCitytDesc : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public AmxApiResponse<OnlineConfigurationDto, Object> getOnlineConfig(String ind) {
		try {

			LOGGER.info("In getOnlineConfig :");
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/onlineconfig/" + ind + "/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<OnlineConfigurationDto, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getOnlineConfig : ", e);
			throw new JaxSystemError();
		} // end of try-catch

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
	public AmxApiResponse<BankBranchDto, Object> getBankBranchList(GetBankBranchRequest request) {
		try {

			LOGGER.info("In getBankBranchList :");
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/bankbranch/get/";
			HttpEntity<GetBankBranchRequest> requestEntity = new HttpEntity<GetBankBranchRequest>(request, getHeader());
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<BankBranchDto, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getBankBranchList : ", e);
			throw new JaxSystemError();
		} // end of try-catc

	}

	/**
	 * <p>
	 * Gives the list of available service groups like bank, cash ,dd etc etc
	 * </p>
	 * 
	 */
	public AmxApiResponse<ServiceGroupMasterDescDto, Object> getServiceGroupList() {
		try {

			LOGGER.info("In getServiceGroupList :");
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/service-group/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<ServiceGroupMasterDescDto, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getServiceGroupList : ", e);
			throw new JaxSystemError();
		} // end of try-catc

	}

	/**
	 * <p>
	 * Gives jax meta parameters like new newBeneTransactionTimeLimit
	 * </p>
	 * 
	 */
	public AmxApiResponse<JaxMetaParameter, Object> getJaxMetaParameter() {
		try {
			LOGGER.info("In getJaxMetaParameter :");
			String url = this.getBaseUrl() + META_API_ENDPOINT + "/meta-parameter/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<JaxMetaParameter, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getJaxMetaParameter : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	/**
	 * 
	 * @return To fetch list of PrefixDTO use getResults method of AmxApiResponse
	 * 
	 */
	public AmxApiResponse<PrefixDTO, Object> getAllPrefix() {
		try {
			return restService.ajax(this.getBaseUrl() + META_API_ENDPOINT + "/prefix/").header(getHeader()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<PrefixDTO, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			throw new JaxSystemError(e);
		}
	}

	/**
	 * 
	 * @return To fetch list of Branch Details
	 * 
	 */

	public AmxApiResponse<BranchDetailDTO, Object> getAllBranchDetail() {
		try {
			LOGGER.info("Get all the Branch Details ");

			String url = this.getBaseUrl() + META_API_ENDPOINT + "/branchdetail/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<BranchDetailDTO, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getAllBranchDetail : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}
}
