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

import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.RemittanceTransactionValidationException;
import com.amx.amxlib.meta.model.RemittanceReceiptSubreport;
import com.amx.amxlib.meta.model.SourceOfIncomeDto;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.PurposeOfTransactionModel;
import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;
import com.amx.amxlib.model.response.RemittanceTransactionResponsetModel;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.client.util.ConverterUtility;


@Component
public class RemitClient extends AbstractJaxServiceClient{
	private Logger log = Logger.getLogger(RemitClient.class);
	
	@Autowired
	private JaxMetaInfo jaxMetaInfo;
	
	@Autowired
	private ConverterUtility util;

	
	public ApiResponse<TransactionHistroyDTO> getTransactionHistroy(String docfyr,String docNumber,String fromDate,String toDate) {
		ResponseEntity<ApiResponse<TransactionHistroyDTO>> response = null;
		try {
			BigDecimal countryId  = jaxMetaInfo.getCountryId();
			BigDecimal customerId = jaxMetaInfo.getCustomerId();
			log.info("Transaction Histroy");
			StringBuffer sb = new StringBuffer();
			sb.append("?docfyr=").append(docfyr).append("&docNumber=").append(docNumber).append("&fromDate=").append(fromDate).append("&toDate=").append(toDate);
			log.info("Input String :"+sb.toString());
			String url =baseUrl.toString()+ REMIT_API_ENDPOINT+"/trnxHist/"+sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<ApiResponse<TransactionHistroyDTO>>(){});
		} catch (Exception e) {
			log.debug("exception in registeruser ", e);
		}
		return response.getBody();
	}
	
	public ApiResponse<RemittanceReceiptSubreport> report(TransactionHistroyDTO transactionHistroyDTO) {
		ResponseEntity<ApiResponse<RemittanceReceiptSubreport>> response = null;
		try {
		BigDecimal countryId  = jaxMetaInfo.getCountryId();
		BigDecimal companyId  = jaxMetaInfo.getCompanyId();
		BigDecimal customerId = jaxMetaInfo.getCustomerId();
		transactionHistroyDTO.setApplicationCountryId(countryId);
		transactionHistroyDTO.setCompanyId(companyId);
		transactionHistroyDTO.setLanguageId(new BigDecimal(1));
		transactionHistroyDTO.setCustomerId(customerId);
		log.info("Remit Client :"+countryId+"\t companyId :"+companyId+"\t customerId :"+customerId);
		HttpEntity<String> requestEntity = new HttpEntity<String>(util.marshall(transactionHistroyDTO), getHeader());
		String sendOtpUrl = baseUrl.toString() + REMIT_API_ENDPOINT+"/remitReport/";
		response = restTemplate.exchange(sendOtpUrl, HttpMethod.POST, requestEntity,new ParameterizedTypeReference<ApiResponse<RemittanceReceiptSubreport>>() {});
		}catch(Exception e) {
			log.error("exception in report ", e);
		}
		return response.getBody();
	}
	
	public ApiResponse<RemittanceTransactionResponsetModel> validateTransaction(
			RemittanceTransactionRequestModel request) throws RemittanceTransactionValidationException, LimitExeededException {

		ResponseEntity<ApiResponse<RemittanceTransactionResponsetModel>> response = null;
		try {
			BigDecimal countryId = jaxMetaInfo.getCountryId();
			BigDecimal companyId = jaxMetaInfo.getCompanyId();
			BigDecimal customerId = jaxMetaInfo.getCustomerId();
			log.info("Remit Client validateTransaction :" + countryId + "\t companyId :" + companyId + "\t customerId :"
					+ customerId);
			HttpEntity<RemittanceTransactionRequestModel> requestEntity = new HttpEntity<RemittanceTransactionRequestModel>(
					request, getHeader());
			String sendOtpUrl = baseUrl.toString() + REMIT_API_ENDPOINT + "/validate/";
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<RemittanceTransactionResponsetModel>>() {
					});
		} catch (Exception e) {
			log.error("exception in validateTransaction ", e);
		}
		validateRemittanceDataValidation(response.getBody());
		return response.getBody();
	}
	
	public ApiResponse<SourceOfIncomeDto> getSourceOfIncome(){
		ResponseEntity<ApiResponse<SourceOfIncomeDto>> response = null;
		try {
		HttpEntity<SourceOfIncomeDto> requestEntity = new HttpEntity<SourceOfIncomeDto>(getHeader());
		String url = baseUrl.toString() + REMIT_API_ENDPOINT + "/sourceofincome/";
		response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<ApiResponse<SourceOfIncomeDto>>() {
				});
		
		}catch(Exception e) {
			log.error("exception in getSourceOfIncome ", e);
		}
		return response.getBody();
	}
	

	public ApiResponse<PurposeOfTransactionModel> getPurposeOfTransactions(BigDecimal beneId) {
		ResponseEntity<ApiResponse<PurposeOfTransactionModel>> response = null;
		try {
			RemittanceTransactionRequestModel request = new RemittanceTransactionRequestModel();
			request.setBeneId(beneId);
			HttpEntity<RemittanceTransactionRequestModel> requestEntity = new HttpEntity<RemittanceTransactionRequestModel>(
					request, getHeader());
			String url = baseUrl.toString() + REMIT_API_ENDPOINT + "/purpose-of-txn/list/";
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<PurposeOfTransactionModel>>() {
					});

		} catch (Exception e) {
			log.error("exception in getPurposeOfTransactions ", e);
		}
		return response.getBody();

	}
	
	public ApiResponse<PurposeOfTransactionModel> saveTransaction(
			RemittanceTransactionRequestModel transactionRequestModel) {
		ResponseEntity<ApiResponse<PurposeOfTransactionModel>> response = null;
		try {

			HttpEntity<RemittanceTransactionRequestModel> requestEntity = new HttpEntity<RemittanceTransactionRequestModel>(
					transactionRequestModel, getHeader());
			String url = baseUrl.toString() + REMIT_API_ENDPOINT + "/save-application/";
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<PurposeOfTransactionModel>>() {
					});

		} catch (Exception e) {
			log.error("exception in saveTransaction ", e);
		}
		return response.getBody();

	}
	

}
