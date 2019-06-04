package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.REMIT_API_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.RemittanceTransactionValidationException;
import com.amx.amxlib.meta.model.CustomerRatingDTO;
import com.amx.amxlib.meta.model.RemittanceReceiptSubreport;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.request.RemittanceTransactionStatusRequestModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.PurposeOfTransactionModel;
import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;
import com.amx.amxlib.model.response.RemittanceTransactionStatusResponseModel;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.client.util.ConverterUtility;
import com.amx.jax.model.request.remittance.IRemitTransReqPurpose;
import com.amx.jax.model.request.remittance.RemittanceTransactionRequestModel;
import com.amx.jax.model.response.SourceOfIncomeDto;
import com.amx.jax.model.response.remittance.RemittanceTransactionResponsetModel;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.rest.RestService;
import com.amx.utils.ArgUtil;

@Component
public class RemitClient extends AbstractJaxServiceClient {
	private static final Logger LOGGER = Logger.getLogger(RemitClient.class);

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	private ConverterUtility util;

	@Autowired
	RestService restService;

	public ApiResponse<TransactionHistroyDTO> getTransactionHistroy(String docfyr, String docNumber, String fromDate,
			String toDate) {
		try {
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
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getTransactionHistroy : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	@Deprecated
	public ApiResponse<RemittanceReceiptSubreport> report(TransactionHistroyDTO transactionHistroyDTO) {
		return this.report(transactionHistroyDTO, false);
	}

	public ApiResponse<RemittanceReceiptSubreport> report(TransactionHistroyDTO transactionHistroyDTO,
			Boolean promotion, JaxMetaInfo jaxMetaInfo) {
		try {
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
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in report : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public ApiResponse<RemittanceReceiptSubreport> report(TransactionHistroyDTO transactionHistroyDTO,
			Boolean promotion) {
		return report(transactionHistroyDTO, promotion, jaxMetaInfo);
	}

	public ApiResponse<RemittanceTransactionResponsetModel> validateTransaction(
			RemittanceTransactionRequestModel request)
			throws RemittanceTransactionValidationException, LimitExeededException {
		try {
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
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in validateTransaction : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public ApiResponse<SourceOfIncomeDto> getSourceOfIncome() {
		try {
			HttpEntity<SourceOfIncomeDto> requestEntity = new HttpEntity<SourceOfIncomeDto>(getHeader());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/sourceofincome/";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<SourceOfIncomeDto>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getSourceOfIncome : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public ApiResponse<PurposeOfTransactionModel> getPurposeOfTransactions(
			IRemitTransReqPurpose remittanceTransactionRequestModel) {
		try {
			HttpEntity<IRemitTransReqPurpose> requestEntity = new HttpEntity<IRemitTransReqPurpose>(
					remittanceTransactionRequestModel, getHeader());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/purpose-of-txn/list/";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<PurposeOfTransactionModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getPurposeOfTransactions : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<PurposeOfTransactionModel> getPurposeOfTransactions(BigDecimal beneId) {
		try {
			RemittanceTransactionRequestModel request = new RemittanceTransactionRequestModel();
			request.setBeneId(beneId);
			HttpEntity<RemittanceTransactionRequestModel> requestEntity = new HttpEntity<RemittanceTransactionRequestModel>(
					request, getHeader());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/purpose-of-txn/list/";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<PurposeOfTransactionModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getPurposeOfTransactions : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<RemittanceApplicationResponseModel> saveTransaction(
			RemittanceTransactionRequestModel transactionRequestModel)
			throws RemittanceTransactionValidationException, LimitExeededException {
		try {
			HttpEntity<RemittanceTransactionRequestModel> requestEntity = new HttpEntity<RemittanceTransactionRequestModel>(
					transactionRequestModel, getHeader());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/save-application/";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<RemittanceApplicationResponseModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in saveTransaction : ", e);
			throw new JaxSystemError();
		} // end of try-catch

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

		try {
			jaxMetaInfo.setCountryId(paymentResponseDto.getApplicationCountryId());
			jaxMetaInfo.setCustomerId(paymentResponseDto.getCustomerId());
			HttpEntity<PaymentResponseDto> requestEntity = new HttpEntity<PaymentResponseDto>(paymentResponseDto,
					getHeader());

			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/save-remittance/";
			LOGGER.info("calling jax url: " + url);
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<PaymentResponseDto>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in saveRemittanceTransaction : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	/**
	 * Fetches the transaction details of given document number and document fin
	 * year
	 */
	public ApiResponse<RemittanceTransactionStatusResponseModel> fetchTransactionDetails(
			RemittanceTransactionStatusRequestModel request, Boolean promotion)
			throws RemittanceTransactionValidationException, LimitExeededException {
		try {
			HttpEntity<RemittanceTransactionStatusRequestModel> requestEntity = new HttpEntity<RemittanceTransactionStatusRequestModel>(
					request, getHeader());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/status/";
			return restService.ajax(url).queryParam("promotion", promotion).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<RemittanceTransactionStatusResponseModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in fetchTransactionDetails : ", e);
			throw new JaxSystemError();
		} // end of try-catch

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

		try {
			jaxMetaInfo.setCountryId(paymentResponseDto.getApplicationCountryId());
			jaxMetaInfo.setCustomerId(paymentResponseDto.getCustomerId());
			HttpEntity<PaymentResponseDto> requestEntity = new HttpEntity<PaymentResponseDto>(paymentResponseDto,
					getHeader());

			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/save-payment-id/";
			LOGGER.info("calling jax url: " + url);
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<PaymentResponseDto>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in savePaymentId : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public AmxApiResponse<CustomerRatingDTO, ?> saveCustomerRating(CustomerRatingDTO customerRatingDTO)
			throws RemittanceTransactionValidationException, LimitExeededException {

		try {
			HttpEntity<CustomerRatingDTO> requestEntity = new HttpEntity<CustomerRatingDTO>(customerRatingDTO,
					getHeader());

			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/save-customer-rating/";
			LOGGER.info(" Calling customer rating :" + customerRatingDTO.toString());
			return restService.ajax(url).post(requestEntity).asApiResponse(CustomerRatingDTO.class);
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in customer rating : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<RemittanceTransactionResponsetModel> calcEquivalentAmount(
			RemittanceTransactionRequestModel request)
			throws RemittanceTransactionValidationException, LimitExeededException {
		try {
			HttpEntity<RemittanceTransactionRequestModel> requestEntity = new HttpEntity<RemittanceTransactionRequestModel>(
					request, getHeader());

			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/calc/";
			LOGGER.info(" Calling calcEquivalentAmount :");
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<RemittanceTransactionResponsetModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in calcEquivalentAmount : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

}
