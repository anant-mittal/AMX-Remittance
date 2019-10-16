package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.REMIT_API_ENDPOINT;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.constant.ApiEndpoint;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.RemittanceTransactionValidationException;
import com.amx.amxlib.meta.model.RemittanceReceiptSubreport;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.request.RemittanceTransactionStatusRequestModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.PurposeOfTransactionModel;
import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;
import com.amx.amxlib.model.response.RemittanceTransactionStatusResponseModel;
import com.amx.amxlib.service.IRemittanceServiceOnline;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.client.util.ConverterUtility;
import com.amx.jax.dict.AmxEnums.Products;
import com.amx.jax.model.customer.CustomerRatingDTO;
import com.amx.jax.model.request.remittance.IRemitTransReqPurpose;
import com.amx.jax.model.request.remittance.RemittanceTransactionDrRequestModel;
import com.amx.jax.model.request.remittance.RemittanceTransactionRequestModel;
import com.amx.jax.model.response.SourceOfIncomeDto;
import com.amx.jax.model.response.remittance.RemittanceTransactionResponsetModel;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.rest.RestService;
import com.amx.libjax.model.jaxfield.JaxConditionalFieldDto;

@Component
public class RemitClient extends AbstractJaxServiceClient implements IRemittanceServiceOnline {
	private static final Logger LOGGER = Logger.getLogger(RemitClient.class);

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	private ConverterUtility util;

	@Autowired
	RestService restService;

	public ApiResponse<TransactionHistroyDTO> getTransactionHistroy(String docfyr, String docNumber, String fromDate,
			String toDate) {
		
			LOGGER.info("Transaction Histroy");
			StringBuilder sb = new StringBuilder();
			sb.append("?docNumber=").append(docNumber).append("&fromDate=").append(fromDate).append("&toDate=")
					.append(toDate);
			if (docfyr != null) {
				sb.append("&docfyr=").append(docfyr);
			}
			LOGGER.info("Input String :" + sb.toString());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/trnxHist/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<TransactionHistroyDTO>>() {
					});
		

	}

	@Deprecated
	public ApiResponse<RemittanceReceiptSubreport> report(TransactionHistroyDTO transactionHistroyDTO) {
		return this.report(transactionHistroyDTO, false);
	}

	public ApiResponse<RemittanceReceiptSubreport> report(TransactionHistroyDTO transactionHistroyDTO,
			Boolean promotion, JaxMetaInfo jaxMetaInfo) {
		
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal companyId = jaxMetaInfo.getCompanyId();
			BigDecimal customerId = jaxMetaInfo.getCustomerId();
			transactionHistroyDTO.setApplicationCountryId(countryId);
			transactionHistroyDTO.setCompanyId(companyId);
			transactionHistroyDTO.setLanguageId(new BigDecimal(1));
			transactionHistroyDTO.setCustomerId(customerId);

			LOGGER.debug("Remit Client :" + countryId + "\t companyId :" + companyId + "\t customerId :" + customerId);
			HttpEntity<String> requestEntity = new HttpEntity<String>(util.marshall(transactionHistroyDTO),
					getHeader(jaxMetaInfo));
			String sendOtpUrl = this.getBaseUrl() + REMIT_API_ENDPOINT + "/remitReport/";
			return restService.ajax(sendOtpUrl).queryParam("promotion", promotion).meta(new JaxMetaInfo())
					.post(requestEntity).as(new ParameterizedTypeReference<ApiResponse<RemittanceReceiptSubreport>>() {
					});
		
	}

	public ApiResponse<RemittanceReceiptSubreport> report(TransactionHistroyDTO transactionHistroyDTO,
			Boolean promotion) {
		return report(transactionHistroyDTO, promotion, jaxMetaInfo);
	}

	public ApiResponse<RemittanceTransactionResponsetModel> validateTransaction(
			RemittanceTransactionRequestModel request)
			throws RemittanceTransactionValidationException, LimitExeededException {
	
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal companyId = jaxMetaInfo.getCompanyId();
			BigDecimal customerId = jaxMetaInfo.getCustomerId();
			LOGGER.info("Remit Client validateTransaction :" + countryId + "\t companyId :" + companyId
					+ "\t customerId :" + customerId);
			HttpEntity<RemittanceTransactionRequestModel> requestEntity = new HttpEntity<RemittanceTransactionRequestModel>(
					request, getHeader());
			String sendOtpUrl = this.getBaseUrl() + REMIT_API_ENDPOINT + "/validate/";
			return restService.ajax(sendOtpUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<RemittanceTransactionResponsetModel>>() {
					});
		
	}

	public ApiResponse<SourceOfIncomeDto> getSourceOfIncome() {
		
			HttpEntity<SourceOfIncomeDto> requestEntity = new HttpEntity<SourceOfIncomeDto>(getHeader());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/sourceofincome/";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<SourceOfIncomeDto>>() {
					});
		
	}

	public ApiResponse<PurposeOfTransactionModel> getPurposeOfTransactions(
			IRemitTransReqPurpose remittanceTransactionRequestModel) {
	
			HttpEntity<IRemitTransReqPurpose> requestEntity = new HttpEntity<IRemitTransReqPurpose>(
					remittanceTransactionRequestModel, getHeader());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/purpose-of-txn/list/";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<PurposeOfTransactionModel>>() {
					});
		

	}

	public ApiResponse<PurposeOfTransactionModel> getPurposeOfTransactions(BigDecimal beneId) {
	
			RemittanceTransactionRequestModel request = new RemittanceTransactionRequestModel();
			request.setBeneId(beneId);
			HttpEntity<RemittanceTransactionRequestModel> requestEntity = new HttpEntity<RemittanceTransactionRequestModel>(
					request, getHeader());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/purpose-of-txn/list/";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<PurposeOfTransactionModel>>() {
					});
		

	}

	public ApiResponse<RemittanceApplicationResponseModel> saveTransaction(
			RemittanceTransactionRequestModel transactionRequestModel)
			throws RemittanceTransactionValidationException, LimitExeededException {
		
			HttpEntity<RemittanceTransactionRequestModel> requestEntity = new HttpEntity<RemittanceTransactionRequestModel>(
					transactionRequestModel, getHeader());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/save-application/";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<RemittanceApplicationResponseModel>>() {
					});

	}
	
	public ApiResponse<RemittanceApplicationResponseModel> saveTransactionV2(
			RemittanceTransactionDrRequestModel transactionRequestModel)
			throws RemittanceTransactionValidationException, LimitExeededException {
		
			HttpEntity<RemittanceTransactionDrRequestModel> requestEntity = new HttpEntity<RemittanceTransactionDrRequestModel>(
					transactionRequestModel, getHeader());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/save-application/v2/";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<RemittanceApplicationResponseModel>>() {
					});

	}
	

	/**
	 * @deprecated - where are we using this method?
	 * 
	 * @param paymentResponseDto
	 * @return
	 * @throws RemittanceTransactionValidationException
	 * @throws LimitExeededException
	 */
	@Deprecated
	public ApiResponse<PaymentResponseDto> saveRemittanceTransaction(PaymentResponseDto paymentResponseDto)
			throws RemittanceTransactionValidationException, LimitExeededException {

			jaxMetaInfo.setCountryId(paymentResponseDto.getApplicationCountryId());
			jaxMetaInfo.setCustomerId(paymentResponseDto.getCustomerId());
			HttpEntity<PaymentResponseDto> requestEntity = new HttpEntity<PaymentResponseDto>(paymentResponseDto,
					getHeader());

			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/save-remittance/";
			LOGGER.info("calling jax url: " + url);
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<PaymentResponseDto>>() {
					});
	}

	/**
	 * Fetches the transaction details of given document number and document fin
	 * year
	 */
	public ApiResponse<RemittanceTransactionStatusResponseModel> fetchTransactionDetails(
			RemittanceTransactionStatusRequestModel request, Boolean promotion)
			throws RemittanceTransactionValidationException, LimitExeededException {
	
			HttpEntity<RemittanceTransactionStatusRequestModel> requestEntity = new HttpEntity<RemittanceTransactionStatusRequestModel>(
					request, getHeader());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/status/";
			return restService.ajax(url).queryParam("promotion", promotion).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<RemittanceTransactionStatusResponseModel>>() {
					});
		
	}

	/**
	 * @deprecated - where are we using this method?
	 * 
	 * @param paymentResponseDto
	 * @return
	 * @throws RemittanceTransactionValidationException
	 * @throws LimitExeededException
	 */
	@Deprecated
	public ApiResponse<PaymentResponseDto> savePaymentId(PaymentResponseDto paymentResponseDto)
			throws RemittanceTransactionValidationException, LimitExeededException {

		
			jaxMetaInfo.setCountryId(paymentResponseDto.getApplicationCountryId());
			jaxMetaInfo.setCustomerId(paymentResponseDto.getCustomerId());
			HttpEntity<PaymentResponseDto> requestEntity = new HttpEntity<PaymentResponseDto>(paymentResponseDto,
					getHeader());

			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/save-payment-id/";
			LOGGER.info("calling jax url: " + url);
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<PaymentResponseDto>>() {
					});
		

	}

	public AmxApiResponse<CustomerRatingDTO, ?> saveCustomerRating(CustomerRatingDTO customerRatingDTO,Products prodType)
			throws RemittanceTransactionValidationException, LimitExeededException {

		
			customerRatingDTO.setProdType(prodType);
			
			HttpEntity<CustomerRatingDTO> requestEntity = new HttpEntity<CustomerRatingDTO>(customerRatingDTO,
					getHeader());

			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/save-customer-rating/";
			LOGGER.info(" Calling customer rating :" + customerRatingDTO.toString());
			return restService.ajax(url).post(requestEntity).asApiResponse(CustomerRatingDTO.class);
		

	}
	
	
	public AmxApiResponse<CustomerRatingDTO, ?> saveFxorderCustomerRating(CustomerRatingDTO customerRatingDTO)
			throws RemittanceTransactionValidationException, LimitExeededException {

		
			HttpEntity<CustomerRatingDTO> requestEntity = new HttpEntity<CustomerRatingDTO>(customerRatingDTO,
					getHeader());

			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/save-fxorder-customer-rating/";
			LOGGER.info(" Calling customer rating :" + customerRatingDTO.toString());
			return restService.ajax(url).post(requestEntity).asApiResponse(CustomerRatingDTO.class);
		

	}
	
	public AmxApiResponse<CustomerRatingDTO, ?> inquireCustomerRating(BigDecimal remittanceTrnxId, String product)
			throws RemittanceTransactionValidationException, LimitExeededException {
	
			CustomerRatingDTO request = new CustomerRatingDTO();
			request.setRemittanceTransactionId(remittanceTrnxId);
			request.setProducttype(product);
			HttpEntity<CustomerRatingDTO> requestEntity = new HttpEntity<CustomerRatingDTO>(
					request, getHeader());

			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/customer-trnx-rating/";
			
			return restService.ajax(url).queryParam("remittanceTrnxId", remittanceTrnxId).queryParam("product", product).post(requestEntity)
					.asApiResponse(CustomerRatingDTO.class);
		

	}
		
	public ApiResponse<RemittanceTransactionResponsetModel> calcEquivalentAmount(
			RemittanceTransactionRequestModel request)
			throws RemittanceTransactionValidationException, LimitExeededException {
	
			HttpEntity<RemittanceTransactionRequestModel> requestEntity = new HttpEntity<RemittanceTransactionRequestModel>(
					request, getHeader());

			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/calc/";
			LOGGER.info(" Calling calcEquivalentAmount :");
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<RemittanceTransactionResponsetModel>>() {
					});
		
	}
	
	@Override
	public AmxApiResponse<RemittanceTransactionResponsetModel, List<JaxConditionalFieldDto>> validateTransactionV2(
			RemittanceTransactionRequestModel model) {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(ApiEndpoint.REMIT_API_ENDPOINT + Path.RATE_ENQUIRY)
					.meta(new JaxMetaInfo()).post(model)
					.as(new ParameterizedTypeReference<AmxApiResponse<RemittanceTransactionResponsetModel, List<JaxConditionalFieldDto>>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in validateTransactionV2 : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	@Override
	public AmxApiResponse<RemittanceTransactionStatusResponseModel, Object> getApplicationStatusByAppId(
			BigDecimal applicationId) {
		try {
			return restService.ajax(appConfig.getJaxURL())
					.path(ApiEndpoint.REMIT_API_ENDPOINT + Path.APPLICATION_STATUS).meta(new JaxMetaInfo())
					.pathParam(Params.APPLICATION_ID, applicationId).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<RemittanceTransactionStatusResponseModel, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getApplicationStatusByAppId : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}



}
