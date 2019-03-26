package com.amx.jax.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dict.AmxEnums;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.services.PaymentService;

@RestController
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@RequestMapping(value = PaymentResponseDto.PAYMENT_CAPTURE_URL, method = RequestMethod.POST)
	public AmxApiResponse<PaymentResponseDto, Object> capturePayment(
			@RequestBody PaymentResponseDto paymentResponse,
			@RequestParam AmxEnums.Products product) {

		if (AmxEnums.Products.REMIT.equals(product)) {
			return paymentService.captrueForRemittance(paymentResponse);
		} else if (AmxEnums.Products.FXORDER.equals(product)) {
			return paymentService.captrueForFxOrder(paymentResponse);
		}

		return null;
	}
}
