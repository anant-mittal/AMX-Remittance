package com.amx.jax.placeorder.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.PlaceOrder;
import com.amx.jax.placeorder.service.PlaceOrderRateAlertService;

import java.math.BigDecimal;
import java.util.List;
import org.apache.log4j.Logger;

@RestController
public class PlaceOrderController {
	
@Autowired
PlaceOrderRateAlertService rateAlertService;	

private static final Logger LOGGER = Logger.getLogger(PlaceOrderController.class);

	@RequestMapping(value= "/rate-alert/placeorder",  method = RequestMethod.GET)
	public List<PlaceOrder> rateAlertPlaceOrder(@RequestParam BigDecimal fromAmount,@RequestParam BigDecimal toAmount, @RequestParam BigDecimal countryId, @RequestParam BigDecimal currencyId,@RequestParam BigDecimal bankId,@RequestParam BigDecimal derivedSellRate) {
		LOGGER.info(String.format(
				"Inside rateAlertPlaceOrder Request with parameters --> Country: %s,Currency: %s, BankName: %s, ExchangeRate: %s",
				fromAmount, toAmount, countryId, currencyId));
				return rateAlertService.rateAlertPlaceOrder(fromAmount,toAmount,countryId,currencyId,bankId,derivedSellRate);
	}
	
}
