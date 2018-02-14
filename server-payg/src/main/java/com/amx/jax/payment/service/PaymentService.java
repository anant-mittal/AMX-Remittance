/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.service;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.meta.model.PaymentResponseDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.client.RemitClient;
import com.amx.jax.payment.model.url.PaymentResponse;
import com.amx.jax.payment.model.url.PaymentResponseData;
import com.amx.jax.scope.TenantContextHolder;

/**
 * @author Viki Sangani 14-Dec-2017 PaymentService.java
 */
@Component
public class PaymentService {

	private static final Logger LOGGER = Logger.getLogger(PaymentService.class);

	@Autowired
	private RemitClient remitClient;

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	public PaymentResponse capturePayment(Map<String, String> paramMap) {

		jaxMetaInfo.setTenant(TenantContextHolder.currentSite());

		PaymentResponseData data = new PaymentResponseData();
		PaymentResponse response = new PaymentResponse();

		try {

			PaymentResponseDto paymentResponseDto = generatePaymentResponseDTO(paramMap);

			LOGGER.info("Calling saveRemittanceTransaction with ...  " + paymentResponseDto.toString());
			ApiResponse<PaymentResponseDto> resp = remitClient.saveRemittanceTransaction(paymentResponseDto);

			if (resp.getResult() != null) {
			    LOGGER.info("PaymentResponseDto values -- CollectionDocumentCode : "
						+ resp.getResult().getCollectionDocumentCode() + " CollectionDocumentNumber : "
						+ resp.getResult().getCollectionDocumentNumber() + " CollectionFinanceYear : "
						+ resp.getResult().getCollectionFinanceYear());
				data.setResponseDTO(resp.getResult());
			}

			StringBuilder sb = new StringBuilder();

			for (Map.Entry<String, String> entry : paramMap.entrySet()) {
				String keyValue = entry.getKey() + "/" + entry.getValue();
				sb.append(keyValue);
				LOGGER.info(keyValue);
			}

			data.setMsg(sb.toString());
			response.setResponseCode("SUCCESS");
			response.setResponseMessage("Payment is captured successfully.");
			response.setData(data);
			response.setError(null);
		} catch (Exception e) {
		    LOGGER.error("Exception while capture payment. : ",e);
			response.setResponseCode("FAIL");
			response.setResponseMessage("Exception while capturing payment.");
		}
		return response;

	}

	public PaymentResponseDto generatePaymentResponseDTO(Map<String, String> params) {
		PaymentResponseDto paymentResponseDto = new PaymentResponseDto();

		paymentResponseDto.setApplicationCountryId(new BigDecimal(params.get("applicationCountryId")));
		paymentResponseDto.setAuth_appNo(params.get("auth_appNo"));
		paymentResponseDto.setTransactionId(params.get("tranId"));
		paymentResponseDto.setResultCode(params.get("result"));
		paymentResponseDto.setPostDate(params.get("postDate"));
		paymentResponseDto.setCustomerId(new BigDecimal(params.get("trackId")));
		paymentResponseDto.setTrackId(params.get("trackId"));
		paymentResponseDto.setReferenceId(params.get("referenceId"));
		paymentResponseDto.setUdf1(params.get("udf1"));
		paymentResponseDto.setUdf2(params.get("udf2"));
		paymentResponseDto.setUdf3(params.get("udf3"));
		paymentResponseDto.setUdf4(params.get("udf4"));
		paymentResponseDto.setUdf5(params.get("udf5"));
		paymentResponseDto.setPaymentId(params.get("paymentId"));

		return paymentResponseDto;
	}

}
