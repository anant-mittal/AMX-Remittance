package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.REMIT_API_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.AbstractException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.RemittanceTransactionValidationException;
import com.amx.amxlib.meta.model.PaymentResponseDto;
import com.amx.amxlib.meta.model.RemittanceReceiptSubreport;
import com.amx.amxlib.meta.model.SourceOfIncomeDto;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.request.RemittanceTransactionStatusRequestModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.PurposeOfTransactionModel;
import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;
import com.amx.amxlib.model.response.RemittanceTransactionResponsetModel;
import com.amx.amxlib.model.response.RemittanceTransactionStatusResponseModel;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.client.util.ConverterUtility;

@Component
public class RemitClient extends AbstractJaxServiceClient {
	private Logger log = Logger.getLogger(RemitClient.class);

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	private ConverterUtility util;

	public ApiResponse<TransactionHistroyDTO> getTransactionHistroy(String docfyr, String docNumber, String fromDate,
			String toDate) {
		try {
			ResponseEntity<ApiResponse<TransactionHistroyDTO>> response = null;
			log.info("Transaction Histroy");
			StringBuffer sb = new StringBuffer();
			sb.append("?docNumber=").append(docNumber).append("&fromDate=").append(fromDate).append("&toDate=")
					.append(toDate);
			if (docfyr != null) {
				sb.append("&docfyr=").append(docfyr);
			}
			log.info("Input String :" + sb.toString());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/trnxHist/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<TransactionHistroyDTO>>() {
					});
			return response.getBody();
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch

	}

	public ApiResponse<RemittanceReceiptSubreport> report(TransactionHistroyDTO transactionHistroyDTO) {
		try {
			ResponseEntity<ApiResponse<RemittanceReceiptSubreport>> response = null;
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal companyId = jaxMetaInfo.getCompanyId();
			BigDecimal customerId = jaxMetaInfo.getCustomerId();
			transactionHistroyDTO.setApplicationCountryId(countryId);
			transactionHistroyDTO.setCompanyId(companyId);
			transactionHistroyDTO.setLanguageId(new BigDecimal(1));
			transactionHistroyDTO.setCustomerId(customerId);
			log.info("Remit Client :" + countryId + "\t companyId :" + companyId + "\t customerId :" + customerId);
			HttpEntity<String> requestEntity = new HttpEntity<String>(util.marshall(transactionHistroyDTO),
					getHeader());
			String sendOtpUrl = this.getBaseUrl() + REMIT_API_ENDPOINT + "/remitReport/";
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<RemittanceReceiptSubreport>>() {
					});
			return response.getBody();
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch

	}

	public ApiResponse<RemittanceTransactionResponsetModel> validateTransaction(
			RemittanceTransactionRequestModel request)
			throws RemittanceTransactionValidationException, LimitExeededException {
		try {
			ResponseEntity<ApiResponse<RemittanceTransactionResponsetModel>> response = null;
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal companyId = jaxMetaInfo.getCompanyId();
			BigDecimal customerId = jaxMetaInfo.getCustomerId();
			log.info("Remit Client validateTransaction :" + countryId + "\t companyId :" + companyId + "\t customerId :"
					+ customerId);
			HttpEntity<RemittanceTransactionRequestModel> requestEntity = new HttpEntity<RemittanceTransactionRequestModel>(
					request, getHeader());
			String sendOtpUrl = this.getBaseUrl() + REMIT_API_ENDPOINT + "/validate/";
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<RemittanceTransactionResponsetModel>>() {
					});
			return response.getBody();
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				log.error("error in validateTransaction" , e);
				throw new JaxSystemError();
			}
		} // end of try-catch
	}

	public ApiResponse<SourceOfIncomeDto> getSourceOfIncome() {
		try {
			ResponseEntity<ApiResponse<SourceOfIncomeDto>> response = null;
			HttpEntity<SourceOfIncomeDto> requestEntity = new HttpEntity<SourceOfIncomeDto>(getHeader());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/sourceofincome/";
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<SourceOfIncomeDto>>() {
					});

			return response.getBody();
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch
	}

	public ApiResponse<PurposeOfTransactionModel> getPurposeOfTransactions(BigDecimal beneId) {
		try {
			ResponseEntity<ApiResponse<PurposeOfTransactionModel>> response = null;
			RemittanceTransactionRequestModel request = new RemittanceTransactionRequestModel();
			request.setBeneId(beneId);
			HttpEntity<RemittanceTransactionRequestModel> requestEntity = new HttpEntity<RemittanceTransactionRequestModel>(
					request, getHeader());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/purpose-of-txn/list/";
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<PurposeOfTransactionModel>>() {
					});

			return response.getBody();
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch

	}

	public ApiResponse<RemittanceApplicationResponseModel> saveTransaction(
			RemittanceTransactionRequestModel transactionRequestModel)
			throws RemittanceTransactionValidationException, LimitExeededException {
		try {
			ResponseEntity<ApiResponse<RemittanceApplicationResponseModel>> response = null;

			HttpEntity<RemittanceTransactionRequestModel> requestEntity = new HttpEntity<RemittanceTransactionRequestModel>(
					transactionRequestModel, getHeader());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/save-application/";
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<RemittanceApplicationResponseModel>>() {
					});

			return response.getBody();
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch

	}

	public ApiResponse<PaymentResponseDto> saveRemittanceTransaction(PaymentResponseDto paymentResponseDto)
			throws RemittanceTransactionValidationException, LimitExeededException {
		try {
			ResponseEntity<ApiResponse<PaymentResponseDto>> response = null;
			jaxMetaInfo.setCountryId(paymentResponseDto.getApplicationCountryId());
			jaxMetaInfo.setCustomerId(paymentResponseDto.getCustomerId());
			HttpEntity<PaymentResponseDto> requestEntity = new HttpEntity<PaymentResponseDto>(paymentResponseDto,
					getHeader());

			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/save-remittance/";
			log.info("calling jax url: " + url);
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<PaymentResponseDto>>() {
					});

			log.info("#####################");
			log.info("PaymentResponseDto values -- CollectionDocumentCode : "
					+ response.getBody().getResult().getCollectionDocumentCode() + " CollectionDocumentNumber : "
					+ response.getBody().getResult().getCollectionDocumentNumber() + " CollectionFinanceYear : "
					+ response.getBody().getResult().getCollectionFinanceYear());
			log.info("#####################");

			return response.getBody();
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch

	}

	public ApiResponse<RemittanceTransactionStatusResponseModel> fetchTransactionDetails(
			RemittanceTransactionStatusRequestModel request)
			throws RemittanceTransactionValidationException, LimitExeededException {
		try {
			ResponseEntity<ApiResponse<RemittanceTransactionStatusResponseModel>> response = null;
			HttpEntity<RemittanceTransactionStatusRequestModel> requestEntity = new HttpEntity<RemittanceTransactionStatusRequestModel>(
					request, getHeader());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/status/";
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<RemittanceTransactionStatusResponseModel>>() {
					});

			return response.getBody();
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch
	}

}
