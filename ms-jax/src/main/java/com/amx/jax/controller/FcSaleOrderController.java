package com.amx.jax.controller;

/**
 * Author : Rabil
 * date		: 03/11/2018
 */
import static com.amx.amxlib.constant.ApiEndpoint.FC_SALE_ENDPOINT;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.request.FcSaleOrderTransactionRequestModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.constant.JaxEvent;
import com.amx.jax.meta.MetaData;
import com.amx.jax.services.FcSaleService;
import com.amx.jax.util.JaxContextUtil;

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
	
/** To get the FC Sale currency List**/
	@RequestMapping(value = "/fc-currency-list/", method = RequestMethod.GET)
	public ApiResponse fcCurrencyList() {
		BigDecimal countryId = metaData.getCountryId();
		ApiResponse response = fcSaleService.getFcSalecurrencyList(countryId);
		return response;
	}
	
	
	/** To get the FC Sale currency wise exchnage rate **/
	@RequestMapping(value = "/fc-sale-xrate/", method = RequestMethod.GET)
	public ApiResponse fcExchangeRate(@RequestParam(value = "fxCurrencyId", required = true) BigDecimal fxCurrencyId) {
		BigDecimal applicationCountryId = metaData.getCountryId();
		BigDecimal countryBranchId = metaData.getCountryBranchId();
		ApiResponse response = fcSaleService.getFcSaleExchangeRate(applicationCountryId, countryBranchId, fxCurrencyId);
		return response;
	}
	
	
	/** To calculate   FC Sale currency wise exchnage rate **/
	
	@RequestMapping(value = "/fc-sale-cal-xrate/", method = RequestMethod.GET)
	public ApiResponse fcExchangeRate(@RequestParam(value = "fxCurrencyId", required = true) BigDecimal fxCurrencyId,
									  @RequestParam(value = "fxAmount", required = true) BigDecimal fcAmount) {
		BigDecimal applicationCountryId = metaData.getCountryId();
		BigDecimal countryBranchId = metaData.getCountryBranchId();
		ApiResponse response = fcSaleService.getFCSaleLcAndFcAmount(applicationCountryId, countryBranchId, fxCurrencyId,fcAmount);
		return response;
	}
	
	
	/** to display the default api **/
	
	
	@RequestMapping(value = "/fc-sale-default/", method = RequestMethod.GET)
	public ApiResponse fcSaleDefaultApi() {
		BigDecimal applicationCountryId = metaData.getCountryId();
		BigDecimal countryBranchId = metaData.getCountryBranchId();
		BigDecimal languageId = metaData.getLanguageId();
		ApiResponse response = fcSaleService.getDefaultFsSale(applicationCountryId,countryBranchId,languageId);
		return response;
	}
	
	

	
	
	@RequestMapping(value = "/fcsale-save-application/", method = RequestMethod.POST)
	public ApiResponse saveApplication(@RequestBody @Valid FcSaleOrderTransactionRequestModel model) {
		JaxContextUtil.setJaxEvent(JaxEvent.CREATE_APPLICATION);
		JaxContextUtil.setRequestModel(model);
		logger.info("In Fc Sale Save-Application with parameters" + model.toString());
		ApiResponse response = fcSaleService.saveApplication(model);
		return response;
	}
	
	
	
	
}
