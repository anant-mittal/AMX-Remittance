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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.amx.amxlib.meta.model.RemittanceReceiptSubreport;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.response.ApiResponse;
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
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			StringBuffer sb = new StringBuffer();
			sb.append("?customerId=").append(customerId).append("&docfyr=").append(docfyr).append("&docNumber=").append(docNumber).append("&fromDate=").append(fromDate).append("&toDate=").append(toDate);
			log.info("Input String :"+sb.toString());
			String url =baseUrl.toString()+ REMIT_API_ENDPOINT+"/trnxHist/"+sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
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
			log.error("exception in saveSecurityQuestions ", e);
		}
		return response.getBody();
	}
	
	

}
