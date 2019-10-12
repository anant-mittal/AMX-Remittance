package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.BANK_MASTER_BY_COUNTRY_API_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.amx.amxlib.constant.ApiEndpoint.MetaApi;
import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.meta.model.ApplicationSetupDTO;
import com.amx.amxlib.meta.model.AuthenticationLimitCheckDTO;
import com.amx.amxlib.meta.model.BankBranchDto;
import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.meta.model.BranchDetailDTO;
import com.amx.amxlib.meta.model.CountryMasterDTO;
import com.amx.amxlib.meta.model.DeclarationDTO;
import com.amx.amxlib.meta.model.JaxMetaParameter;
import com.amx.amxlib.meta.model.MultiCountryDTO;
import com.amx.amxlib.meta.model.PrefixDTO;
import com.amx.amxlib.meta.model.ServiceGroupMasterDescDto;
import com.amx.amxlib.meta.model.TermsAndConditionDTO;
import com.amx.amxlib.meta.model.UserFinancialYearDTO;
import com.amx.amxlib.meta.model.ViewAreaDto;
import com.amx.amxlib.meta.model.ViewCityDto;
import com.amx.amxlib.meta.model.ViewDistrictDto;
import com.amx.amxlib.meta.model.ViewGovernateAreaDto;
import com.amx.amxlib.meta.model.ViewGovernateDto;
import com.amx.amxlib.meta.model.ViewStateDto;
import com.amx.amxlib.meta.model.ViewStatusDto;
import com.amx.amxlib.meta.model.WhyDoAskInformationDTO;
import com.amx.amxlib.model.CountryBranchDTO;
import com.amx.amxlib.model.OnlineConfigurationDto;
import com.amx.amxlib.model.request.GetBankBranchRequest;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.model.auth.QuestModelDTO;
import com.amx.jax.model.response.BranchSystemDetailDto;
import com.amx.jax.model.response.CurrencyMasterDTO;
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

	public AmxApiResponse<ApplicationSetupDTO, Object> getApplicationCountry() {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.APPL_COUNTRY)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ApplicationSetupDTO, Object>>() {
					});
		
	}

	public AmxApiResponse<ApplicationSetupDTO, Object> getApplicationCountryByCountryAndCompany() {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.APPL_COUNTRY_COMP)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ApplicationSetupDTO, Object>>() {
					});
		
	}

	public AmxApiResponse<CountryMasterDTO, Object> getAllCountry() {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_COUNTRY)
					.meta(new JaxMetaInfo())
					.get().as(new ParameterizedTypeReference<AmxApiResponse<CountryMasterDTO, Object>>() {
					});
		
	}

	@Deprecated
	public AmxApiResponse<CountryMasterDTO, Object> getAllCountryByLanguageId(String languageId) {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_COUNTRY_BY_lANG_ID)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CountryMasterDTO, Object>>() {
					});
		
	}

	public AmxApiResponse<CountryMasterDTO, Object> getAllCountryByLanguageId(String languageId, String countryId) {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_COUNTRY_BY_lANG_COUNTRY_ID)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CountryMasterDTO, Object>>() {
					});
		
	}

	public AmxApiResponse<CountryMasterDTO, Object> getBusinessCountry() {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_COUNTRY_BC)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CountryMasterDTO, Object>>() {
					});
		
	}

	public AmxApiResponse<QuestModelDTO, Object> getSequrityQuestion() {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.SEQ_QUEST_LIST)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<QuestModelDTO, Object>>() {
					});
		
	}

	public AmxApiResponse<QuestModelDTO, Object> getSequrityQuestionById(String questionId) {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.SEQ_QUEST_BY_ID)
					.pathParam(MetaApi.PARAM_QUEST_ID, questionId).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<QuestModelDTO, Object>>() {
					});
		
	}

	/**
	 * Gives terms and conditions based on metadata like lang id etc
	 */
	public AmxApiResponse<TermsAndConditionDTO, Object> getTermsAndCondition() {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_TERMS_BY_lANG_ID)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<TermsAndConditionDTO, Object>>() {
					});
		
	}

	public AmxApiResponse<TermsAndConditionDTO, Object> getTermsAndConditionAsPerCountry() {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_TERMS_BY_lANG_COUNTRY_ID)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<TermsAndConditionDTO, Object>>() {
					});
		
	}

	public AmxApiResponse<WhyDoAskInformationDTO, Object> getWhyAskInfo() {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_WHY)
					.meta(new JaxMetaInfo())
					.get().as(new ParameterizedTypeReference<AmxApiResponse<WhyDoAskInformationDTO, Object>>() {
					});
		
	}

	public AmxApiResponse<UserFinancialYearDTO, Object> getFinancialYear() {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_FYEAR)
					.meta(new JaxMetaInfo())
					.get().as(new ParameterizedTypeReference<AmxApiResponse<UserFinancialYearDTO, Object>>() {
					});
		
	}

	public AmxApiResponse<AuthenticationLimitCheckDTO, Object> getContactUsTime() {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.HELP_DESK_TIME)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<AuthenticationLimitCheckDTO, Object>>() {
					});
		
	}

	public AmxApiResponse<AuthenticationLimitCheckDTO, Object> getHelpDeskNo() {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_HELP_NO)
					.meta(new JaxMetaInfo())
					.get().as(new ParameterizedTypeReference<AmxApiResponse<AuthenticationLimitCheckDTO, Object>>() {
					});
		
	}

	public AmxApiResponse<MultiCountryDTO, Object> getMultiCountryList() {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.MULTI_COUNTRY)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<MultiCountryDTO, Object>>() {
					});
		
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
	 * @param serviceGroupId       - bank or cash
	 * @param routingBankId        - service provider id in case of cash or bank id
	 *                             in case of bank
	 * @return CurrencyMasterDTO
	 */
	public AmxApiResponse<CurrencyMasterDTO, Object> getBeneficiaryCurrency(BigDecimal beneficiaryCountryId,
			BigDecimal serviceGroupId, BigDecimal routingBankId) {
		ResponseEntity<AmxApiResponse<CurrencyMasterDTO, Object>> response;
		
			LOGGER.info("in getBeneficiaryCurrency");
			String url = this.getBaseUrl() + MetaApi.PREFIX + "/currency/beneservice/";

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
					.queryParam("beneficiaryCountryId", beneficiaryCountryId)
					.queryParam("serviceGroupId", serviceGroupId).queryParam("routingBankId", routingBankId);
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<AmxApiResponse<CurrencyMasterDTO, Object>>() {
					});

		
		return response.getBody();
	}

	public AmxApiResponse<CurrencyMasterDTO, Object> getAllOnlineCurrency() {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.CURRENCY_ONLINE)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CurrencyMasterDTO, Object>>() {
					});
		
	}

	// added by chetan 30/04/2018 list the country for currency.
	public AmxApiResponse<CurrencyMasterDTO, Object> getAllExchangeRateCurrencyList() {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.EXCHANGE_RATE_CURRENCY_LIST)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CurrencyMasterDTO, Object>>() {
					});
		
	}

	public AmxApiResponse<CurrencyMasterDTO, Object> getCurrencyByCountryId(BigDecimal countryId) {
		
			LOGGER.info("in getAllOnlineCurrency");
			String url = this.getBaseUrl() + MetaApi.PREFIX + "/currency/bycountry/" + countryId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<CurrencyMasterDTO, Object>>() {
					});
		

	}

	public AmxApiResponse<ViewDistrictDto, Object> getDistricDesc(BigDecimal stateId, BigDecimal districtId) {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_DISTRICTDESC)
					.pathParam(MetaApi.PARAM_STATE_ID, stateId).pathParam(MetaApi.PARAM_DISTRICT_ID, districtId)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ViewDistrictDto, Object>>() {
					});
		
	}

	/**
	 * <p>
	 * Gives the list of banks by country
	 * </p>
	 * 
	 * @param countryId - Id value of country
	 */
	public AmxApiResponse<BankMasterDTO, Object> getBankListForCountry(BigDecimal countryId) {
		
			LOGGER.info("in getBankListForCountry");

			String endpoint = MetaApi.PREFIX + BANK_MASTER_BY_COUNTRY_API_ENDPOINT;
			endpoint = endpoint.replaceAll("\\{country\\-id\\}", countryId.toPlainString());
			String url = this.getBaseUrl() + endpoint;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<BankMasterDTO, Object>>() {
					});
		

	}

	public AmxApiResponse<ViewDistrictDto, Object> getDistrictList(BigDecimal stateId) {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_DISTRICTLIST)
					.pathParam(MetaApi.PARAM_STATE_ID, stateId).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ViewDistrictDto, Object>>() {
					});
		
	}

	public AmxApiResponse<ViewStateDto, Object> getStateList(BigDecimal countryId) {
		

			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (BigDecimal.ZERO.equals(languageId)) {
				languageId = new BigDecimal(1);
			}
			String url = this.getBaseUrl() + MetaApi.PREFIX + "/statelist/" + countryId + "/";
			return restService.ajax(url).meta(new JaxMetaInfo()).get().asApiResponse(ViewStateDto.class);
		

	}

	public AmxApiResponse<ViewStateDto, Object> getStateDesc(BigDecimal countryId, BigDecimal stateId) {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_STATE_DESC)
					.pathParam(MetaApi.PARAM_COUNTRY_ID, countryId).pathParam(MetaApi.PARAM_STATE_ID, stateId)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ViewStateDto, Object>>() {
					});
		
	}

	public AmxApiResponse<ViewCityDto, Object> getCitytList(BigDecimal districtId) {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_CITY_LIST)
					.pathParam(MetaApi.PARAM_DISTRICT_ID, districtId).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ViewCityDto, Object>>() {
					});
		
	}

	public AmxApiResponse<ViewCityDto, Object> getCitytDesc(BigDecimal districtId, BigDecimal cityId) {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_CITY_DESC)
					.pathParam(MetaApi.PARAM_DISTRICT_ID, districtId).pathParam(MetaApi.PARAM_CITY_ID, cityId)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ViewCityDto, Object>>() {
					});
		
	}

	public AmxApiResponse<OnlineConfigurationDto, Object> getOnlineConfig(String ind) {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_ONLINE_CONFIG)
					.pathParam(MetaApi.PARAM_IND, ind).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<OnlineConfigurationDto, Object>>() {
					});
		
	}

	/**
	 * <p>
	 * Gives the list of bank branches based on parameters like ifsc, swift, name
	 * etc
	 * </p>
	 * 
	 * @param object of type {@code GetBankBranchRequest}
	 */
	public AmxApiResponse<BankBranchDto, Object> getBankBranchList(GetBankBranchRequest request) {
		
			LOGGER.info("In getBankBranchList :");
			String url = this.getBaseUrl() + MetaApi.PREFIX + "/bankbranch/get/";
			HttpEntity<GetBankBranchRequest> requestEntity = new HttpEntity<GetBankBranchRequest>(request, getHeader());
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<BankBranchDto, Object>>() {
					});

	}

	/**
	 * <p>
	 * Gives the list of available service groups like bank, cash ,dd etc etc
	 * </p>
	 * 
	 */
	public AmxApiResponse<ServiceGroupMasterDescDto, Object> getServiceGroupList() {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.SERVICE_GROUP)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ServiceGroupMasterDescDto, Object>>() {
					});
		
	}

	/**
	 * <p>
	 * Gives jax meta parameters like new newBeneTransactionTimeLimit
	 * </p>
	 * 
	 */
	public AmxApiResponse<JaxMetaParameter, Object> getJaxMetaParameter() {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.META_PARAMETER)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<JaxMetaParameter, Object>>() {
					});
		
	}

	/**
	 * 
	 * @return To fetch list of PrefixDTO use getResults method of AmxApiResponse
	 * 
	 */
	public AmxApiResponse<PrefixDTO, Object> getAllPrefix() {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_PREFIX)
					.meta(new JaxMetaInfo())
					.get().as(new ParameterizedTypeReference<AmxApiResponse<PrefixDTO, Object>>() {
					});
		
	}

	/**
	 * 
	 * @return To fetch list of Branch Details
	 * 
	 */
	public AmxApiResponse<BranchDetailDTO, Object> getAllBranchDetail() {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_BRANCH_DETAIL)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<BranchDetailDTO, Object>>() {
					});
		
	}

	/**
	 * 
	 * @return To fetch list of Branch inventory details
	 * 
	 */
	public AmxApiResponse<BranchSystemDetailDto, Object> listBranchSystemInventory() {
		
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_BRANCH_SYSTEM_INV_LIST)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<BranchSystemDetailDto, Object>>() {
					});
		
	}

	public AmxApiResponse<TermsAndConditionDTO, Object> getTermsAndConditionAsPerCountryForFxOrder() {
		
			return restService.ajax(appConfig.getJaxURL())
					.path(MetaApi.PREFIX + MetaApi.API_TERMS_BY_lANG_COUNTRY_ID_FOR_FX).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<TermsAndConditionDTO, Object>>() {
					});
		
	}
	
	
	@Deprecated
	public AmxApiResponse<ViewAreaDto, Object> getAreaList() {
		
			return restService.ajax(appConfig.getJaxURL())
					.path(MetaApi.PREFIX + MetaApi.API_AREA_LIST).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ViewAreaDto, Object>>() {
					});
		
	}
	
	
	public AmxApiResponse<ViewStatusDto, Object> getStatusList() {
		
			return restService.ajax(appConfig.getJaxURL())
					.path(MetaApi.PREFIX + MetaApi.API_STATUS_LIST).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ViewStatusDto, Object>>() {
					});
		
	}
	
	public AmxApiResponse<CountryBranchDTO, Object> getCountryBranchList() {
		
			return restService.ajax(appConfig.getJaxURL())
					.path(MetaApi.PREFIX + MetaApi.API_COUNTRY_BRANCH_LIST).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CountryBranchDTO, Object>>() {
					});
		
	}
	
	
	public AmxApiResponse<ViewGovernateDto, Object> getGovernateList() {
		
			return restService.ajax(appConfig.getJaxURL())
					.path(MetaApi.PREFIX + MetaApi.API_GOVERNATE_LIST).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ViewGovernateDto, Object>>() {
					});
		
	}
	

	public AmxApiResponse<ViewGovernateAreaDto, Object> getGovernateAreaList(BigDecimal governateId) {
		
			LOGGER.debug("in governateId :"+governateId);
			return restService.ajax(appConfig.getJaxURL())
					.path(MetaApi.PREFIX + MetaApi.API_GOVERNATE_AREA_LIST).meta(new JaxMetaInfo()).get()
					.queryParam(MetaApi.PARAM_GOVERONATE_ID, governateId).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ViewGovernateAreaDto, Object>>() {
					});
		
	}
	
	
	public AmxApiResponse<DeclarationDTO, Object> getDeclaration() {
		

			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_DECLARATION)
					.meta(new JaxMetaInfo()).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<DeclarationDTO, Object>>() {
					});
		
	}
	
	
}
