package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.ACCOUNT_TYPE_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.BENE_API_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.GET_AGENT_BRANCH_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.GET_AGENT_MASTER_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.GET_SERVICE_PROVIDER_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.REMIT_API_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.SEND_OTP_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.UPDAE_STATUS_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.VALIDATE_OTP_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.amx.amxlib.constant.BeneficiaryConstant.BeneStatus;
import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.CustomerValidationException;
import com.amx.amxlib.exception.IncorrectInputException;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.UnknownJaxError;
import com.amx.amxlib.meta.model.AccountTypeDto;
import com.amx.amxlib.meta.model.CountryMasterDTO;
import com.amx.amxlib.meta.model.RemittancePageDto;
import com.amx.amxlib.meta.model.RoutingBankMasterDTO;
import com.amx.amxlib.model.AbstractUserModel;
import com.amx.amxlib.model.BeneAccountModel;
import com.amx.amxlib.model.BenePersonalDetailModel;
import com.amx.amxlib.model.BeneRelationsDescriptionDto;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.JaxTransactionResponse;
import com.amx.amxlib.model.trnx.BeneficiaryTrnxModel;
import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterAgentBranchParam;
import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterAgentParam;
import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterServiceProviderParam;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.client.util.ConverterUtility;
import com.amx.jax.model.BeneficiaryListDTO;
import com.amx.jax.rest.RestService;
import com.amx.utils.JsonUtil;

@Component
public class BeneClient extends AbstractJaxServiceClient {

	private static final Logger LOGGER = Logger.getLogger(BeneClient.class);

	@Autowired
	private ConverterUtility util;

	@Autowired
	private RestService restService;

	/**
	 * sdsd
	 * 
	 * @param beneCountryId
	 * @return
	 */
	public ApiResponse<BeneficiaryListDTO> getBeneficiaryList(BigDecimal beneCountryId,Boolean excludePackage) {
		
			return restService.ajax(appConfig.getJaxURL() + BENE_API_ENDPOINT + "/beneList/")
					.queryParam("beneCountryId", beneCountryId)
					.queryParam("excludePackage", excludePackage)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
					});
		
	}

	public ApiResponse<CountryMasterDTO> getBeneficiaryCountryList(BigDecimal beneCountryId) {
		
			ResponseEntity<ApiResponse<CountryMasterDTO>> response;
			StringBuffer sb = new StringBuffer();
			sb.append("?beneCountryId=").append(beneCountryId);
			// LOGGER.info("Bene Clinet to get bene list Input String :" + sb.toString());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/bene/country/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CountryMasterDTO>>() {
					});
			return response.getBody();
		
	}

	// returning beneficiary country list with channeling
	/**
	 * 
	 * @author Chetan Pawar
	 * @param beneCountryId remove 11-05-2018 - beneficiaryCountryId
	 * @return beneficiaryCountry List
	 */
	public ApiResponse<CountryMasterDTO> getBeneficiaryCountryList() {
		
			ResponseEntity<ApiResponse<CountryMasterDTO>> response;
			// StringBuffer sb = new StringBuffer();
			// sb.append("?beneCountryId=").append(beneCountryId);
			// LOGGER.info("Bene Clinet to get bene list Input String :" + sb.toString());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/bene/country/";// + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CountryMasterDTO>>() {
					});
			return response.getBody();
		
	}

	public ApiResponse<RemittancePageDto> defaultBeneficiary(BigDecimal beneRealtionId, BigDecimal transactionId) {
		
			ResponseEntity<ApiResponse<RemittancePageDto>> response;
			return restService.ajax(appConfig.getJaxURL()).path(BENE_API_ENDPOINT + "/defaultbene/")
					.queryParam("beneRelationId", beneRealtionId)
					.queryParam("transactionId", transactionId)
					.meta(new JaxMetaInfo()).post()
					.as(new ParameterizedTypeReference<ApiResponse<RemittancePageDto>>() {
					});
	}

	@Deprecated
	public ApiResponse<BeneficiaryListDTO> beneDisable(BigDecimal beneMasSeqId, String remarks) {
		
			ResponseEntity<ApiResponse<BeneficiaryListDTO>> response;

			LOGGER.info("Transaction Histroy");

			StringBuffer sb = new StringBuffer();
			sb.append("?beneMasSeqId=").append(beneMasSeqId).append("&remarks=").append(remarks);
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/disable/" + sb.toString();
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
					});
		
	}

	public ApiResponse<BeneficiaryListDTO> beneFavoriteUpdate(BigDecimal beneMasSeqId) {
		

			LOGGER.info("bene client beneFavoriteUpdate ");

			StringBuffer sb = new StringBuffer();
			sb.append("?beneMasSeqId=").append(beneMasSeqId);
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/favoritebeneupdate/" + sb.toString();
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
					});
		
	}

	/** favouritebene **/
	public ApiResponse<BeneficiaryListDTO> beneFavoriteList() {
		

			MultiValueMap<String, String> headers = getHeader();
			LOGGER.info("beneFavList  Clinet to get bene list Input String :");
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/favouritebenelist/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
					});
		
	}

	public ApiResponse<BeneficiaryListDTO> beneUpdate(BeneficiaryListDTO beneficiarydto) {
		
			LOGGER.info("Bene update Client :" + beneficiarydto.getCustomerId() + "\t customerId :"
					+ beneficiarydto.getBeneficiaryRelationShipSeqId());
			HttpEntity<String> requestEntity = new HttpEntity<String>(util.marshall(beneficiarydto), getHeader());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/beneupdate/";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
					});
		
	}

	public ApiResponse<AccountTypeDto> getBeneficiaryAccountType(BigDecimal beneCountryId) {
		
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + ACCOUNT_TYPE_ENDPOINT;
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("countryId", beneCountryId);
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(builder.build().encode().toUri()).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<AccountTypeDto>>() {
					});
		
	}

	/**
	 * @return Gives the list of all beneficiary relations possible
	 */
	public ApiResponse<BeneRelationsDescriptionDto> getBeneficiaryRelations() {
		
			LOGGER.info("in getBeneficiaryRelations");
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/relations/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BeneRelationsDescriptionDto>>() {
					});
		
	}

	public ApiResponse<CivilIdOtpModel> sendOtp()
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		
			HttpEntity<AbstractUserModel> requestEntity = new HttpEntity<AbstractUserModel>(getHeader());
			String sendOtpUrl = this.getBaseUrl() + BENE_API_ENDPOINT + SEND_OTP_ENDPOINT;
			return restService.ajax(sendOtpUrl).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
		
	}

	public ApiResponse<CustomerModel> validateOtp(String mOtp, String eOtp)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException, UnknownJaxError {
		
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String validateOtpUrl = this.getBaseUrl() + BENE_API_ENDPOINT + VALIDATE_OTP_ENDPOINT;
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(validateOtpUrl).queryParam("mOtp", mOtp)
					.queryParam("eOtp", eOtp);
			return restService.ajax(builder.build().encode().toUri()).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		
	}

	@SuppressWarnings("rawtypes")
	public ApiResponse updateStatus(BigDecimal beneMasSeqId, String remarks, BeneStatus status, String mOtp,
			String eOtp) {
	
			StringBuffer sb = new StringBuffer();
			sb.append("?beneMasSeqId=").append(beneMasSeqId);
			sb.append("&remarks=").append(remarks);
			sb.append("&status=").append(status);
			sb.append("&mOtp=").append(mOtp);
			sb.append("&eOtp=").append(eOtp);

			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + UPDAE_STATUS_ENDPOINT + sb.toString();
			return restService.ajax(url).post(requestEntity).as(new ParameterizedTypeReference<ApiResponse>() {
			});
		
	}

	/**
	 * Saves beneficiary bank account details in transaction
	 * 
	 * @param beneAccountModel - Bene account model
	 */
	public ApiResponse<JaxTransactionResponse> saveBeneAccountInTrnx(BeneAccountModel beneAccountModel) {
		
			HttpEntity<BeneAccountModel> requestEntity = new HttpEntity<BeneAccountModel>(beneAccountModel,
					getHeader());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/trnx/bene/bene-account/";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<JaxTransactionResponse>>() {
					});
		
	}

	/**
	 * Saves beneficiary personal details like contact, names etc
	 * 
	 * @param benePersonalDetailModel - BenePersonalDetail Model
	 */
	public ApiResponse<JaxTransactionResponse> saveBenePersonalDetailInTrnx(
			BenePersonalDetailModel benePersonalDetailModel) {
		
			HttpEntity<BenePersonalDetailModel> requestEntity = new HttpEntity<BenePersonalDetailModel>(
					benePersonalDetailModel, getHeader());
			LOGGER.info("Model is "+JsonUtil.toJson(benePersonalDetailModel));
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/trnx/bene/bene-details/";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<JaxTransactionResponse>>() {
					});
		
	}

	/**
	 * Commits the add beneficiary transaction in database by validating otp passed
	 * 
	 * @param mOtp - mobile otp
	 * @param eOtp - email otp
	 */
	public ApiResponse<BeneficiaryTrnxModel> commitAddBeneTrnx(String mOtp, String eOtp) {
		
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String baseUrl = this.getBaseUrl() + BENE_API_ENDPOINT + "/trnx/addbene/commit/";
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl).queryParam("mOtp", mOtp)
					.queryParam("eOtp", eOtp);
			return restService.ajax(builder.build().encode().toUri()).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BeneficiaryTrnxModel>>() {
					});
		
	}

	/**
	 * Fetch list of service provider
	 * 
	 * @param param - object with routingCountryId and ServiceGroupId fields
	 *              populated
	 */
	public ApiResponse<RoutingBankMasterDTO> getServiceProvider(RoutingBankMasterServiceProviderParam param) {

	
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + GET_SERVICE_PROVIDER_ENDPOINT;
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
					.queryParam("beneCountryId", param.getRoutingCountryId())
					.queryParam("serviceGroupId", param.getServiceGroupId());
			return restService.ajax(builder.build().encode().toUri()).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<RoutingBankMasterDTO>>() {
					});
		
	}

	/**
	 * Fetch list of agent master
	 * 
	 * @param param - object with routingCountryId, routingBankId, currencyId,
	 *              serviceGroupId fields populated
	 * 
	 */
	public ApiResponse<RoutingBankMasterDTO> getAgentMaster(RoutingBankMasterAgentParam param) {

		
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + GET_AGENT_MASTER_ENDPOINT;
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
					.queryParam("beneCountryId", param.getRoutingCountryId())
					.queryParam("serviceGroupId", param.getServiceGroupId())
					.queryParam("routingBankId", param.getRoutingBankId())
					.queryParam("currencyId", param.getCurrencyId());
			return restService.ajax(builder.build().encode().toUri()).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<RoutingBankMasterDTO>>() {
					});
		
	}

	/**
	 * Fetch list of agent master
	 * 
	 * @param param - object with routingCountryId, routingBankId, currencyId,
	 *              agentBankId and ServiceGroupId fields populated
	 * 
	 */
	public ApiResponse<RoutingBankMasterDTO> getAgentBranch(RoutingBankMasterAgentBranchParam param) {

		
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + GET_AGENT_BRANCH_ENDPOINT;
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
					.queryParam("beneCountryId", param.getRoutingCountryId())
					.queryParam("serviceGroupId", param.getServiceGroupId())
					.queryParam("routingBankId", param.getRoutingBankId())
					.queryParam("agentBankId", param.getAgentBankId()).queryParam("currencyId", param.getCurrencyId());
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(builder.build().encode().toUri()).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<RoutingBankMasterDTO>>() {
					});
		
	}

	public ApiResponse<RemittancePageDto> poBeneficiary(BigDecimal placeOrderId) {
		
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/pobene/";
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
					.queryParam("placeOrderId", placeOrderId);
			return restService.ajax(builder.build().encode().toUri()).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<RemittancePageDto>>() {
					});
		
	}
}
