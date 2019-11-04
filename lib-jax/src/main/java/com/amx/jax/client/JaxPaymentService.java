package com.amx.jax.client;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.dict.AmxEnums;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.rest.RestService;

@Component
public class JaxPaymentService {

	private static final Logger LOGGER = LoggerService.getLogger(JaxPaymentService.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	public AmxApiResponse<PaymentResponseDto, Object> payment(AmxEnums.Products product,
			PaymentResponseDto paymentResponseDto) {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(PaymentResponseDto.PAYMENT_CAPTURE_URL)
					.queryParam("product", product)
					.post(paymentResponseDto).meta(new JaxMetaInfo())
					.as(new ParameterizedTypeReference<AmxApiResponse<PaymentResponseDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in payment : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch

	}

}
