package com.amx.jax.payment.service;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.AppConstants;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dict.Channel;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.payment.PaymentConstant;
import com.amx.jax.payment.gateway.PayGConfig;
import com.amx.jax.payment.gateway.PayGResponse;
import com.amx.jax.rest.RequestMetaInfo;
import com.amx.jax.rest.RestService;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Viki Sangani 14-Dec-2017
 * @author Lalit Tanwar
 */
@Component
public class PaymentService {

	private static final Logger LOGGER = Logger.getLogger(PaymentService.class);

	@Autowired
	PayGConfig payGConfig;

	/**
	 * Captures the payment in jax service
	 * 
	 * @param payGServiceResponse - populated response recieved from PayMentgateWaye
	 *                            service
	 * @param product
	 * @param channel
	 * @return
	 */
	public PaymentResponseDto capturePayment(PayGResponse payGServiceResponse, Channel channel, Object product) {
		PaymentResponseDto paymentResponseDto = null;
		try {
			paymentResponseDto = generatePaymentResponseDTO(payGServiceResponse);
			LOGGER.info("Calling saveRemittanceTransaction with ...  " + paymentResponseDto.toString());
			AmxApiResponse<PaymentResponseDto, Object> resp = savePayMentDetails(paymentResponseDto, channel, product);
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

	@Autowired
	RestService restService;
	@Autowired
	AppConfig appConfig;

	public AmxApiResponse<PaymentResponseDto, Object> savePayMentDetails(PaymentResponseDto paymentResponseDto,
			Channel channel, Object product)
			throws Exception {
		try {
			RequestMetaInfo metaInfo = new RequestMetaInfo();
			HttpHeaders headers = new HttpHeaders();
			metaInfo.setTenant(TenantContextHolder.currentSite());
			metaInfo.setCountryId(paymentResponseDto.getApplicationCountryId());
			metaInfo.setCustomerId(paymentResponseDto.getCustomerId());
			headers.add(AppConstants.META_XKEY, new ObjectMapper().writeValueAsString(metaInfo));

			if (ArgUtil.isEmpty(product)) {
				return restService.ajax(appConfig.getJaxURL() + "/remit/save-remittance/")
						.post(new HttpEntity<PaymentResponseDto>(paymentResponseDto, headers))
						.as(new ParameterizedTypeReference<AmxApiResponse<PaymentResponseDto, Object>>() {
						});
			} else {
				return restService.ajax(appConfig.getJaxURL()).path(PaymentResponseDto.PAYMENT_CAPTURE_URL)
						.queryParam(PaymentConstant.Params.PRODUCT, product)
						.queryParam(PaymentConstant.Params.CHANNEL, channel)
						.post(new HttpEntity<PaymentResponseDto>(paymentResponseDto, headers))
						.as(new ParameterizedTypeReference<AmxApiResponse<PaymentResponseDto, Object>>() {
						});
			}

		} catch (Exception e) {
			LOGGER.error("exception in saveRemittanceTransaction : ", e);
			return AmxApiException.evaluate(e);
		} // end of try-catch

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

		paymentResponseDto
				.setCollectionFinanceYear(ArgUtil.parseAsBigDecimal(payGServiceResponse.getCollectionFinYear()));
		paymentResponseDto
				.setCollectionDocumentCode(ArgUtil.parseAsBigDecimal(payGServiceResponse.getCollectionDocCode()));
		paymentResponseDto
				.setCollectionDocumentNumber(ArgUtil.parseAsBigDecimal(payGServiceResponse.getCollectionDocNumber()));

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
		paymentResponseDto.setErrorCategory(payGServiceResponse.getErrorCategory());
		return paymentResponseDto;
	}

}
