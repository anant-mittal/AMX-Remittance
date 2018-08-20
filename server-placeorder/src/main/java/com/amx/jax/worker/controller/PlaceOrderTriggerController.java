package com.amx.jax.worker.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.worker.service.PlaceOrderRateAlertService;

import java.math.BigDecimal;
import org.apache.log4j.Logger;

@RestController
public class PlaceOrderTriggerController {
	
@Autowired
PlaceOrderRateAlertService rateAlertService;


private static final Logger LOGGER = Logger.getLogger(PlaceOrderTriggerController.class);

	@RequestMapping(value= "/rate-alert/placeorder",  method = RequestMethod.GET)
	public ApiResponse rateAlertPlaceOrder(@RequestParam BigDecimal fromAmount,@RequestParam BigDecimal toAmount, @RequestParam BigDecimal countryId, @RequestParam BigDecimal currencyId,@RequestParam BigDecimal bankId,@RequestParam BigDecimal derivedSellRate) {
		LOGGER.info(String.format(
				"Inside rateAlertPlaceOrder Request with parameters --> CountryId: %s,CurrencyId: %s, BankId: %s, ExchangeRate: %s",
				countryId, currencyId, bankId, derivedSellRate));
				return  rateAlertService.rateAlertPlaceOrder(fromAmount,toAmount,countryId,currencyId,bankId,derivedSellRate);
	}
	
	
	@RequestMapping(value= "/exrate-placeorder" , method =RequestMethod.GET)
    public String setExchangeRate()
    {
		return  rateAlertService.setExchangeRate();
    }
}
