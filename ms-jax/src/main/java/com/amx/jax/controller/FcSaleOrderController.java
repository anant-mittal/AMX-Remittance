package com.amx.jax.controller;

/**
 * Author : Rabil
 * date		: 03/11/2018
 */
import static com.amx.amxlib.constant.ApiEndpoint.FC_SALE_ENDPOINT;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.model.PurposeOfTransactionDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.DevicePairOtpResponse;
import com.amx.jax.services.FcSaleService;

@RestController
@RequestMapping(FC_SALE_ENDPOINT)
@SuppressWarnings("rawtypes")
public class FcSaleOrderController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	FcSaleService fcSaleService;
	
	@Autowired
	MetaData metaData;
	
	/**
	 * @return : To get the fx purpose of trnx list
	 */
	
	@RequestMapping(value = "/fc-purposeof-trnx/", method = RequestMethod.GET)
	public ApiResponse fxPurposeofTrnx() {
		ApiResponse response = fcSaleService.getPurposeofTrnxList();
		return response;
	}
	

	@RequestMapping(value = "/fc-currency-list/", method = RequestMethod.GET)
	public ApiResponse fcCurrencyList() {
		BigDecimal countryId = metaData.getCountryId();
		ApiResponse response = fcSaleService.getFcSalecurrencyList(countryId);
		return response;
	}
	
	
	@RequestMapping(value = "/fc-sale-xrate/", method = RequestMethod.GET)
	public ApiResponse fcExchangeRate(@RequestParam(value = "fxCurrencyId", required = true) BigDecimal fxCurrencyId) {
		BigDecimal applicationCountryId = metaData.getCountryId();
		BigDecimal countryBranchId = metaData.getCountryBranchId();
		ApiResponse response = fcSaleService.getFcSaleExchangeRate(applicationCountryId, countryBranchId, fxCurrencyId);
		return response;
	}
	
	
}
