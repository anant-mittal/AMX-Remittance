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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.amx.amxlib.constant.BeneficiaryConstant.BeneStatus;
import com.amx.amxlib.exception.AbstractException;
import com.amx.amxlib.exception.CustomerValidationException;
import com.amx.amxlib.exception.IncorrectInputException;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.UnknownJaxError;
import com.amx.amxlib.meta.model.AccountTypeDto;
import com.amx.amxlib.meta.model.BeneCountryDTO;
import com.amx.amxlib.meta.model.BeneficiaryListDTO;
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
import com.amx.jax.client.util.ConverterUtility;

@Component
public class BeneClient extends AbstractJaxServiceClient {

	private static final Logger LOGGER = Logger.getLogger(BeneClient.class);

	@Autowired
	private ConverterUtility util;

	/**
	 * sdsd
	 * 
	 * @param beneCountryId
	 * @return
	 */
	public ApiResponse<BeneficiaryListDTO> getBeneficiaryList(BigDecimal beneCountryId) {
		try {
			ResponseEntity<ApiResponse<BeneficiaryListDTO>> response;
			MultiValueMap<String, String> headers = getHeader();
			StringBuffer sb = new StringBuffer();
			sb.append("?beneCountryId=").append(beneCountryId);
			LOGGER.info("Bene Clinet to get bene list Input String :" + sb.toString());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/beneList/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
					});

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getBeneficiaryList : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public ApiResponse<BeneCountryDTO> getBeneficiaryCountryList(BigDecimal beneCountryId) {
		try {
			ResponseEntity<ApiResponse<BeneCountryDTO>> response;
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			StringBuffer sb = new StringBuffer();
			sb.append("?beneCountryId=").append(beneCountryId);
			LOGGER.info("Bene Clinet to get bene list Input String :" + sb.toString());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/benecountry/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BeneCountryDTO>>() {
					});

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getBeneficiaryCountryList : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public ApiResponse<RemittancePageDto> defaultBeneficiary(BigDecimal beneRealtionId, BigDecimal transactionId) {
		try {
			ResponseEntity<ApiResponse<RemittancePageDto>> response;

			LOGGER.info("Default Beneficiary");

			StringBuffer sb = new StringBuffer();

			if (beneRealtionId != null || transactionId != null) {
				sb.append("?");
			}

			if (beneRealtionId != null) {
				sb.append("beneRelationId=").append(beneRealtionId).append("&");
			}
			if (transactionId != null) {
				sb.append("transactionId=").append(transactionId);
			}

			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/defaultbene/" + sb.toString();
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<RemittancePageDto>>() {
					});

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in defaultBeneficiary : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	@Deprecated
	public ApiResponse<BeneficiaryListDTO> beneDisable(BigDecimal beneMasSeqId, String remarks) {
		try {
			ResponseEntity<ApiResponse<BeneficiaryListDTO>> response;

			LOGGER.info("Transaction Histroy");

			StringBuffer sb = new StringBuffer();
			sb.append("?beneMasSeqId=").append(beneMasSeqId).append("&remarks=").append(remarks);
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/disable/" + sb.toString();
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
					});

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in beneDisable : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public ApiResponse<BeneficiaryListDTO> beneFavoriteUpdate(BigDecimal beneMasSeqId) {
		try {
			ResponseEntity<ApiResponse<BeneficiaryListDTO>> response;

			LOGGER.info("bene client beneFavoriteUpdate ");

			StringBuffer sb = new StringBuffer();
			sb.append("?beneMasSeqId=").append(beneMasSeqId);
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/favoritebeneupdate/" + sb.toString();
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
					});

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in beneFavoriteUpdate : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	/** favouritebene **/
	public ApiResponse<BeneficiaryListDTO> beneFavoriteList() {
		try {
			ResponseEntity<ApiResponse<BeneficiaryListDTO>> response;
			MultiValueMap<String, String> headers = getHeader();
			LOGGER.info("beneFavList  Clinet to get bene list Input String :");
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/favouritebenelist/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in beneFavoriteList : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public ApiResponse<BeneficiaryListDTO> beneUpdate(BeneficiaryListDTO beneficiarydto) {
		try {
			ResponseEntity<ApiResponse<BeneficiaryListDTO>> response;

			LOGGER.info("Bene update Client :" + beneficiarydto.getCustomerId() + "\t customerId :"
					+ beneficiarydto.getBeneficiaryRelationShipSeqId());
			HttpEntity<String> requestEntity = new HttpEntity<String>(util.marshall(beneficiarydto), getHeader());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/beneupdate/";
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
					});

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in beneUpdate : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public ApiResponse<AccountTypeDto> getBeneficiaryAccountType(BigDecimal beneCountryId) {
		try {
			ResponseEntity<ApiResponse<AccountTypeDto>> response;
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + ACCOUNT_TYPE_ENDPOINT;
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("countryId", beneCountryId);
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<AccountTypeDto>>() {
					});

			return response.getBody();
		} catch (AbstractException ae) {
			ae.printStackTrace();
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getBeneficiaryAccountType : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	/**
	 * @return Gives the list of all beneficiary relations possible
	 */
	public ApiResponse<BeneRelationsDescriptionDto> getBeneficiaryRelations() {
		try {
			ResponseEntity<ApiResponse<BeneRelationsDescriptionDto>> response;

			LOGGER.info("in getBeneficiaryRelations");
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/relations/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BeneRelationsDescriptionDto>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getBeneficiaryRelations : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public ApiResponse<CivilIdOtpModel> sendOtp()
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		try {
			ResponseEntity<ApiResponse<CivilIdOtpModel>> response;
			HttpEntity<AbstractUserModel> requestEntity = new HttpEntity<AbstractUserModel>(getHeader());
			String sendOtpUrl = this.getBaseUrl() + BENE_API_ENDPOINT + SEND_OTP_ENDPOINT;
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
			LOGGER.info("responce from  sendOtp api: " + util.marshall(response.getBody()));

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in sendOtp : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public ApiResponse<CustomerModel> validateOtp(String mOtp, String eOtp)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException, UnknownJaxError {
		try {
			ResponseEntity<ApiResponse<CustomerModel>> response;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String validateOtpUrl = this.getBaseUrl() + BENE_API_ENDPOINT + VALIDATE_OTP_ENDPOINT;
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(validateOtpUrl).queryParam("mOtp", mOtp)
					.queryParam("eOtp", eOtp);
			response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in validateOtp : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	@SuppressWarnings("rawtypes")
	public ApiResponse updateStatus(BigDecimal beneMasSeqId, String remarks, BeneStatus status) {
		try {
			ResponseEntity<ApiResponse> response;

			StringBuffer sb = new StringBuffer();
			sb.append("?beneMasSeqId=").append(beneMasSeqId);
			sb.append("&remarks=").append(remarks);
			sb.append("&status=").append(status);
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + UPDAE_STATUS_ENDPOINT + sb.toString();
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in updateStatus : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	/**
	 * Saves beneficiary bank account details in transaction
	 * 
	 * @param beneAccountModel
	 *            - Bene account model
	 */
	public ApiResponse<JaxTransactionResponse> saveBeneAccountInTrnx(BeneAccountModel beneAccountModel) {
		try {
			ResponseEntity<ApiResponse<JaxTransactionResponse>> response;

			HttpEntity<BeneAccountModel> requestEntity = new HttpEntity<BeneAccountModel>(beneAccountModel,
					getHeader());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/trnx/bene/bene-account/";
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<JaxTransactionResponse>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in saveBeneAccountInTrnx : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	/**
	 * Saves beneficiary personal details like contact, names etc
	 * 
	 * @param benePersonalDetailModel
	 *            - BenePersonalDetail Model
	 */
	public ApiResponse<JaxTransactionResponse> saveBenePersonalDetailInTrnx(
			BenePersonalDetailModel benePersonalDetailModel) {
		try {
			ResponseEntity<ApiResponse<JaxTransactionResponse>> response;

			HttpEntity<BenePersonalDetailModel> requestEntity = new HttpEntity<BenePersonalDetailModel>(
					benePersonalDetailModel, getHeader());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/trnx/bene/bene-details/";
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<JaxTransactionResponse>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in saveBenePersonalDetailInTrnx : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	/**
	 * Commits the add beneficiary transaction in database by validating otp passed
	 * 
	 * @param mOtp
	 *            - mobile otp
	 * @param eOtp
	 *            - email otp
	 */
	public ApiResponse<BeneficiaryTrnxModel> commitAddBeneTrnx(String mOtp, String eOtp) {
		try {
			ResponseEntity<ApiResponse<BeneficiaryTrnxModel>> response;

			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String baseUrl = this.getBaseUrl() + BENE_API_ENDPOINT + "/trnx/addbene/commit/";
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl).queryParam("mOtp", mOtp)
					.queryParam("eOtp", eOtp);
			response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BeneficiaryTrnxModel>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in commitAddBeneTrnx : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	/**
	 * Fetch list of service provider
	 * 
	 * @param param
	 *            - object with routingCountryId and ServiceGroupId fields populated
	 */
	public ApiResponse<RoutingBankMasterDTO> getServiceProvider(RoutingBankMasterServiceProviderParam param) {

		try {
			ResponseEntity<ApiResponse<RoutingBankMasterDTO>> response;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + GET_SERVICE_PROVIDER_ENDPOINT;
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
					.queryParam("beneCountryId", param.getRoutingCountryId())
					.queryParam("serviceGroupId", param.getServiceGroupId());
			response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<RoutingBankMasterDTO>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getServiceProvider : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	/**
	 * Fetch list of agent master
	 * 
	 * @param param
	 *            - object with routingCountryId, routingBankId, currencyId,
	 *            serviceGroupId fields populated
	 * 
	 */
	public ApiResponse<RoutingBankMasterDTO> getAgentMaster(RoutingBankMasterAgentParam param) {

		try {
			ResponseEntity<ApiResponse<RoutingBankMasterDTO>> response;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + GET_AGENT_MASTER_ENDPOINT;
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
					.queryParam("beneCountryId", param.getRoutingCountryId())
					.queryParam("serviceGroupId", param.getServiceGroupId())
					.queryParam("routingBankId", param.getRoutingBankId())
					.queryParam("currencyId", param.getCurrencyId());
			response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<RoutingBankMasterDTO>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getAgentMaster : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	/**
	 * Fetch list of agent master
	 * 
	 * @param param
	 *            - object with routingCountryId, routingBankId, currencyId,
	 *            agentBankId and ServiceGroupId fields populated
	 * 
	 */
	public ApiResponse<RoutingBankMasterDTO> getAgentBranch(RoutingBankMasterAgentBranchParam param) {

		try {
			ResponseEntity<ApiResponse<RoutingBankMasterDTO>> response;
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + GET_AGENT_BRANCH_ENDPOINT;
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
					.queryParam("beneCountryId", param.getRoutingCountryId())
					.queryParam("serviceGroupId", param.getServiceGroupId())
					.queryParam("routingBankId", param.getRoutingBankId())
					.queryParam("agentBankId", param.getAgentBankId()).queryParam("currencyId", param.getCurrencyId());
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<RoutingBankMasterDTO>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getAgentMaster : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}
}
