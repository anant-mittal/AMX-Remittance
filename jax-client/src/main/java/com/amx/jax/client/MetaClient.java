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
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.APPL_COUNTRY)
					.filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ApplicationSetupDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getApplicationCountry : ", ae);
			return JaxSystemError.evaluate(ae);
		} // end of try-catch
	}

	public AmxApiResponse<ApplicationSetupDTO, Object> getApplicationCountryByCountryAndCompany() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.APPL_COUNTRY_COMP)
					.filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ApplicationSetupDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getApplicationCountryByCountryAndCompany : ", ae);
			return JaxSystemError.evaluate(ae);
		} // end of try-catch
	}

	public AmxApiResponse<CountryMasterDTO, Object> getAllCountry() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_COUNTRY).filter(metaFilter)
					.get().as(new ParameterizedTypeReference<AmxApiResponse<CountryMasterDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getAllCountry : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	@Deprecated
	public AmxApiResponse<CountryMasterDTO, Object> getAllCountryByLanguageId(String languageId) {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_COUNTRY_BY_lANG_ID)
					.filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CountryMasterDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getAllCountryByLanguageId : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	public AmxApiResponse<CountryMasterDTO, Object> getAllCountryByLanguageId(String languageId, String countryId) {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_COUNTRY_BY_lANG_COUNTRY_ID)
					.filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CountryMasterDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getAllCountryByLanguageId : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	public AmxApiResponse<CountryMasterDTO, Object> getBusinessCountry() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_COUNTRY_BC)
					.filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CountryMasterDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getBusinessCountry : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	public AmxApiResponse<QuestModelDTO, Object> getSequrityQuestion() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.SEQ_QUEST_LIST)
					.filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<QuestModelDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getSequrityQuestion : ", ae);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public AmxApiResponse<QuestModelDTO, Object> getSequrityQuestionById(String questionId) {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.SEQ_QUEST_BY_ID)
					.pathParam(MetaApi.PARAM_QUEST_ID, questionId).filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<QuestModelDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getSequrityQuestionById : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	/**
	 * Gives terms and conditions based on metadata like lang id etc
	 */
	public AmxApiResponse<TermsAndConditionDTO, Object> getTermsAndCondition() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_TERMS_BY_lANG_ID)
					.filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<TermsAndConditionDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getTermsAndCondition : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	public AmxApiResponse<TermsAndConditionDTO, Object> getTermsAndConditionAsPerCountry() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_TERMS_BY_lANG_COUNTRY_ID)
					.filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<TermsAndConditionDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getTermsAndConditionAsPerCountry : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	public AmxApiResponse<WhyDoAskInformationDTO, Object> getWhyAskInfo() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_WHY).filter(metaFilter)
					.get().as(new ParameterizedTypeReference<AmxApiResponse<WhyDoAskInformationDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getWhyAskInfo : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	public AmxApiResponse<UserFinancialYearDTO, Object> getFinancialYear() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_FYEAR).filter(metaFilter)
					.get().as(new ParameterizedTypeReference<AmxApiResponse<UserFinancialYearDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getFinancialYear : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	public AmxApiResponse<AuthenticationLimitCheckDTO, Object> getContactUsTime() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.HELP_DESK_TIME)
					.filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<AuthenticationLimitCheckDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getContactUsTime : ", ae);
			return JaxSystemError.evaluate(ae);
		} // end of try-catch
	}

	public AmxApiResponse<AuthenticationLimitCheckDTO, Object> getHelpDeskNo() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_HELP_NO).filter(metaFilter)
					.get().as(new ParameterizedTypeReference<AmxApiResponse<AuthenticationLimitCheckDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getHelpDeskNo : ", ae);
			return JaxSystemError.evaluate(ae);
		} // end of try-catch
	}

	public AmxApiResponse<MultiCountryDTO, Object> getMultiCountryList() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.MULTI_COUNTRY)
					.filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<MultiCountryDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getMultiCountryList : ", ae);
			return JaxSystemError.evaluate(ae);
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
			String url = this.getBaseUrl() + MetaApi.PREFIX + "/currency/beneservice/";

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
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.CURRENCY_ONLINE)
					.filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CurrencyMasterDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getAllOnlineCurrency : ", ae);
			return JaxSystemError.evaluate(ae);
		} // end of try-catch
	}

	// added by chetan 30/04/2018 list the country for currency.
	public AmxApiResponse<CurrencyMasterDTO, Object> getAllExchangeRateCurrencyList() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.EXCHANGE_RATE_CURRENCY_LIST)
					.filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CurrencyMasterDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getAllExchangeRateCurrencyList : ", ae);
			return JaxSystemError.evaluate(ae);
		} // end of try-catch
	}

	public AmxApiResponse<CurrencyMasterDTO, Object> getCurrencyByCountryId(BigDecimal countryId) {
		try {
			LOGGER.info("in getAllOnlineCurrency");
			String url = this.getBaseUrl() + MetaApi.PREFIX + "/currency/bycountry/" + countryId;
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
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_DISTRICTDESC)
					.pathParam(MetaApi.PARAM_STATE_ID, stateId).pathParam(MetaApi.PARAM_DISTRICT_ID, districtId)
					.filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ViewDistrictDto, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getDistricDesc : ", ae);
			return JaxSystemError.evaluate(ae);
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

			String endpoint = MetaApi.PREFIX + BANK_MASTER_BY_COUNTRY_API_ENDPOINT;
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
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_DISTRICTLIST)
					.pathParam(MetaApi.PARAM_STATE_ID, stateId).filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ViewDistrictDto, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getDistrictList : ", ae);
			return JaxSystemError.evaluate(ae);
		} // end of try-catch
	}

	public AmxApiResponse<ViewStateDto, Object> getStateList(BigDecimal countryId) {
		try {

			BigDecimal languageId = jaxMetaInfo.getLanguageId();
			if (BigDecimal.ZERO.equals(languageId)) {
				languageId = new BigDecimal(1);
			}
			String url = this.getBaseUrl() + MetaApi.PREFIX + "/statelist/" + countryId + "/";
			return restService.ajax(url).filter(metaFilter).get().asApiResponse(ViewStateDto.class);
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getStateList : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public AmxApiResponse<ViewStateDto, Object> getStateDesc(BigDecimal countryId, BigDecimal stateId) {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_STATE_DESC)
					.pathParam(MetaApi.PARAM_COUNTRY_ID, countryId).pathParam(MetaApi.PARAM_STATE_ID, stateId)
					.filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ViewStateDto, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getStateDesc : ", ae);
			return JaxSystemError.evaluate(ae);
		} // end of try-catch
	}

	public AmxApiResponse<ViewCityDto, Object> getCitytList(BigDecimal districtId) {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_CITY_LIST)
					.pathParam(MetaApi.PARAM_DISTRICT_ID, districtId).filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ViewCityDto, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getCitytList : ", ae);
			return JaxSystemError.evaluate(ae);
		} // end of try-catch
	}

	public AmxApiResponse<ViewCityDto, Object> getCitytDesc(BigDecimal districtId, BigDecimal cityId) {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_CITY_DESC)
					.pathParam(MetaApi.PARAM_DISTRICT_ID, districtId).pathParam(MetaApi.PARAM_CITY_ID, cityId)
					.filter(metaFilter).get().as(new ParameterizedTypeReference<AmxApiResponse<ViewCityDto, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getCitytDesc : ", ae);
			return JaxSystemError.evaluate(ae);
		} // end of try-catch
	}

	public AmxApiResponse<OnlineConfigurationDto, Object> getOnlineConfig(String ind) {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_ONLINE_CONFIG)
					.pathParam(MetaApi.PARAM_IND, ind).filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<OnlineConfigurationDto, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getOnlineConfig : ", ae);
			return JaxSystemError.evaluate(ae);
		}
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
			String url = this.getBaseUrl() + MetaApi.PREFIX + "/bankbranch/get/";
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
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.SERVICE_GROUP)
					.filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ServiceGroupMasterDescDto, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getServiceGroupList : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	/**
	 * <p>
	 * Gives jax meta parameters like new newBeneTransactionTimeLimit
	 * </p>
	 * 
	 */
	public AmxApiResponse<JaxMetaParameter, Object> getJaxMetaParameter() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.META_PARAMETER)
					.filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<JaxMetaParameter, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getJaxMetaParameter : ", ae);
			return JaxSystemError.evaluate(ae);
		} // end of try-catch
	}

	/**
	 * 
	 * @return To fetch list of PrefixDTO use getResults method of AmxApiResponse
	 * 
	 */
	public AmxApiResponse<PrefixDTO, Object> getAllPrefix() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_PREFIX).filter(metaFilter)
					.get().as(new ParameterizedTypeReference<AmxApiResponse<PrefixDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getAllPrefix : ", ae);
			return JaxSystemError.evaluate(ae);
		} // end of try-catch
	}

	/**
	 * 
	 * @return To fetch list of Branch Details
	 * 
	 */
	public AmxApiResponse<BranchDetailDTO, Object> getAllBranchDetail() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_BRANCH_DETAIL)
					.filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<BranchDetailDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getAllBranchDetail : ", ae);
			return JaxSystemError.evaluate(ae);
		} // end of try-catch
	}
	
	/**
	 * 
	 * @return To fetch list of Branch inventory details
	 * 
	 */
	public AmxApiResponse<BranchSystemDetailDto, Object> listBranchSystemInventory() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_BRANCH_SYSTEM_INV_LIST)
					.filter(metaFilter).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<BranchSystemDetailDto, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in listBranchSystemInventory : ", ae);
			return JaxSystemError.evaluate(ae);
		} // end of try-catch
	}

	public AmxApiResponse<TermsAndConditionDTO, Object> getTermsAndConditionAsPerCountryForFxOrder() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(MetaApi.PREFIX + MetaApi.API_TERMS_BY_lANG_COUNTRY_ID_FOR_FX).filter(metaFilter).get().as(new ParameterizedTypeReference<AmxApiResponse<TermsAndConditionDTO, Object>>() {});
		} catch (Exception ae) {
			LOGGER.error("exception in getTermsAndConditionAsPerCountry : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

}
