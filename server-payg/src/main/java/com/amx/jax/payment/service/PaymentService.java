package com.amx.jax.payment.service;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.meta.model.PaymentResponseDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.client.RemitClient;
import com.amx.jax.payment.gateway.PayGResponse;
import com.amx.jax.scope.TenantContextHolder;

/**
 * @author Viki Sangani 14-Dec-2017
 * @author Lalit Tanwar
 */
@Component
public class PaymentService {

	private static final Logger LOGGER = Logger.getLogger(PaymentService.class);

	@Autowired
	private RemitClient remitClient;

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	/**
	 * Captures the payment in jax service
	 * 
	 * @param payGServiceResponse
	 *            - populated response recieved from PayMentgateWaye service
	 * @return
	 */
	public PaymentResponseDto capturePayment(PayGResponse payGServiceResponse) {
		jaxMetaInfo.setTenant(TenantContextHolder.currentSite());

		PaymentResponseDto paymentResponseDto = null;
		try {
			paymentResponseDto = generatePaymentResponseDTO(payGServiceResponse);
			LOGGER.info("Calling saveRemittanceTransaction with ...  " + paymentResponseDto.toString());
			ApiResponse<PaymentResponseDto> resp = remitClient.saveRemittanceTransaction(paymentResponseDto);
			if (resp.getResult() != null) {
				LOGGER.info("PaymentResponseDto values -- CollectionDocumentCode : "
						+ resp.getResult().getCollectionDocumentCode() + " CollectionDocumentNumber : "
						+ resp.getResult().getCollectionDocumentNumber() + " CollectionFinanceYear : "
						+ resp.getResult().getCollectionFinanceYear());
				return resp.getResult();
			}
		} catch (Exception e) {
			LOGGER.error("Exception while capture payment. : ", e);
		}
		return paymentResponseDto;
	}

	/**
	 * 
	 * @param payGServiceResponse
	 * @return
	 */
	public PaymentResponseDto generatePaymentResponseDTO(PayGResponse payGServiceResponse) {
		PaymentResponseDto paymentResponseDto = new PaymentResponseDto();

		if (payGServiceResponse.getCountryId() != null) {
			paymentResponseDto.setApplicationCountryId(new BigDecimal(payGServiceResponse.getCountryId()));
		}

		paymentResponseDto.setAuth_appNo(payGServiceResponse.getAuth());
		paymentResponseDto.setTransactionId(payGServiceResponse.getTranxId());
		paymentResponseDto.setResultCode(payGServiceResponse.getResult());
		paymentResponseDto.setPostDate(payGServiceResponse.getPostDate());

		if (payGServiceResponse.getTrackId() != null) {
			paymentResponseDto.setCustomerId(new BigDecimal(payGServiceResponse.getTrackId()));
		}

		paymentResponseDto.setTrackId(payGServiceResponse.getTrackId());
		paymentResponseDto.setReferenceId(payGServiceResponse.getRef());

		paymentResponseDto.setUdf1(payGServiceResponse.getUdf1());
		paymentResponseDto.setUdf2(payGServiceResponse.getUdf2());
		paymentResponseDto.setUdf3(payGServiceResponse.getUdf3());
		paymentResponseDto.setUdf4(payGServiceResponse.getUdf4());
		paymentResponseDto.setUdf5(payGServiceResponse.getUdf5());
		paymentResponseDto.setPaymentId(payGServiceResponse.getPaymentId());
		paymentResponseDto.setErrorText(payGServiceResponse.getErrorText());
		paymentResponseDto.setError(payGServiceResponse.getError());
		return paymentResponseDto;
	}

}
