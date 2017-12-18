package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.EXCHANGE_RATE_ENDPOINT;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.exrateservice.service.ExchangeRateService;

@RestController
@RequestMapping(EXCHANGE_RATE_ENDPOINT)
@SuppressWarnings("rawtypes")
public class ExchangeRateController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	ExchangeRateService service;

	@RequestMapping(value = "/online/", method = RequestMethod.GET)
	public ApiResponse getOnlineExchangeRates(BigDecimal fromCurrency, BigDecimal toCurrency, BigDecimal amount,
			@RequestParam(required = false) BigDecimal bankId) {
		logger.debug("getExchangeRates Request: fromCurrency" + fromCurrency + " toCurrency " + toCurrency + " amount: "
				+ amount);
		ApiResponse response = service.getExchangeRatesForOnline(fromCurrency, toCurrency, amount, bankId);
		return response;
	}
}
