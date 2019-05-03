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
import com.amx.jax.exrateservice.service.NewExchangeRateService;

@RestController
@RequestMapping(EXCHANGE_RATE_ENDPOINT)
@SuppressWarnings("rawtypes")
public class ExchangeRateController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	NewExchangeRateService service;

	@RequestMapping(value = "/online/", method = RequestMethod.GET)
	public ApiResponse getOnlineExchangeRates(BigDecimal fromCurrency, BigDecimal toCurrency, BigDecimal amount,
			@RequestParam(required = false) BigDecimal bankId,
			@RequestParam(required = false) BigDecimal beneBankCountryId) {
		logger.info("getExchangeRates Request: fromCurrency" + fromCurrency + " toCurrency " + toCurrency + " amount: "
				+ amount);
		ApiResponse response = service.getExchangeRatesForOnline(fromCurrency, toCurrency, amount, bankId,
				beneBankCountryId);
		return response;
	}

	@RequestMapping(value = "/online/", method = RequestMethod.POST)
	public ApiResponse setOnlineExchangeRates(@RequestParam(required = true) String quoteName,
			@RequestParam BigDecimal value) {
		ApiResponse response = service.setOnlineExchangeRates(quoteName, value);
		return response;
	}
	
	@RequestMapping(value = "/min-max/exchangerate/", method = RequestMethod.GET)
	public ApiResponse getMinMaxExrate() {
		logger.info("In min Max Exchange rate...");
		ApiResponse response = service.getMinMaxExrate();
		return response;
	}
	
	@RequestMapping(value = "/online/placeorder", method = RequestMethod.POST)
	public ApiResponse setOnlineExchangeRatesPlaceorder(@RequestParam(required = true) String quoteName,@RequestParam BigDecimal bankId,
			@RequestParam BigDecimal value) {
		ApiResponse response = service.setOnlineExchangeRatesPlaceorder(quoteName, bankId, value);
		return response;
	}
}
