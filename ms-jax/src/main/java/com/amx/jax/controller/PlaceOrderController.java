package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.PLACE_ORDER_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.constant.JaxEvent;
import com.amx.jax.dbmodel.PlaceOrder;
import com.amx.jax.meta.MetaData;
import com.amx.jax.services.PlaceOrderService;
import com.amx.jax.util.JaxContextUtil;

/**
 * @author Subodh Bhoir
 *
 */
@RestController
@RequestMapping(PLACE_ORDER_ENDPOINT)
@SuppressWarnings("rawtypes")
public class PlaceOrderController {
	
	private Logger logger = Logger.getLogger(PlaceOrderController.class);
	
	@Autowired
	PlaceOrderService placeOrderService;
	
	@Autowired
	MetaData metaData;
	
	/**
	 *  saves place order
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ApiResponse handleUrlSave(@RequestBody PlaceOrderDTO dto) 
	{
		logger.info("In save with parameters" + dto.toString());
		BigDecimal customerId = metaData.getCustomerId();
		
		dto.setCustomerId(customerId);
		ApiResponse response = null;
		response = placeOrderService.savePlaceOrder(dto);
		return response;
	}

	/**
	 * get place order list for customer
	 * @return
	 */
	@RequestMapping(value = "/get/placeOrder/forCustomer", method = RequestMethod.POST)
	public ApiResponse handleUrlgetPlaceOrder() {
		
		BigDecimal customerId = metaData.getCustomerId();
		logger.info("In /getPlaceOrder with customerId :" + customerId );
		
		ApiResponse response = null;
		response = placeOrderService.getPlaceOrderForCustomer(customerId);
		return response;
		
	}
	
	/**
	 * Get All Place Order list
	 * @return
	 */
	@RequestMapping(value = "/getAll/placeOrder", method = RequestMethod.POST)
	public ApiResponse handleUrlgetAllPlaceOrder() {
		logger.info("In /getAll place orders ");
		
		ApiResponse response = null;
		response = placeOrderService.getAllPlaceOrder();
		return response;
	}
	
	/**
	 * delete place order
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ApiResponse handleUrlDeletePlaceOrder(@RequestBody PlaceOrderDTO dto) {
		logger.info("In delete with parameter" + dto.toString());
		BigDecimal customerId = metaData.getCustomerId();
		
		dto.setCustomerId(customerId);
		ApiResponse response = null;
		response = placeOrderService.deletePlaceOrder(dto);
		return response;
		
	}

	/**
	 * get place order by placeOrderId
	 * @return
	 */
	@RequestMapping(value = "/get/placeOrder/forId", method = RequestMethod.POST)
	public ApiResponse handleUrlgetPlaceOrderId(@RequestBody PlaceOrderDTO dto) {
		
		logger.info("In /getPlaceOrder with customerId :" + dto.toString());
		BigDecimal placeOrderId = dto.getPlaceOrderId();
		
		ApiResponse response = null;
		response = placeOrderService.getPlaceOrderForId(placeOrderId);
		return response;
		
	}
	
	/**
	 * update place order 
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ApiResponse handleUrlUpdatePlaceOrder(@RequestBody PlaceOrderDTO dto) {
		logger.info("In Update Place Order" + dto.toString());
		BigDecimal customerId = metaData.getCustomerId();
		
		dto.setCustomerId(customerId);
		ApiResponse response = null;
		response = placeOrderService.updatePlaceOrder(dto);
		
		return response;
	}
	
	/**
	 * update place order 
	 * @return
	 */
	@RequestMapping(value = "/get/placeorder/ontrigger", method = RequestMethod.GET)
	public ApiResponse handleUrlPlaceOrderOnTrigger(@RequestParam BigDecimal fromAmount,@RequestParam BigDecimal toAmount, @RequestParam BigDecimal countryId, @RequestParam BigDecimal currencyId,@RequestParam BigDecimal bankId,@RequestParam BigDecimal derivedSellRate) {
		logger.info(String.format(
				"Inside PlaceOrderOnTrigger Request with parameters --> CountryId: %s,CurrencyId: %s, BankId: %s, ExchangeRate: %s",
				countryId, currencyId, bankId, derivedSellRate));
		PlaceOrder placeOrder =new PlaceOrder();
		placeOrder.setCountryId(countryId);
		placeOrder.setCurrencyId(currencyId);
		placeOrder.setBankId(bankId);
		placeOrder.setTargetExchangeRate(derivedSellRate);
		/*JaxContextUtil.setJaxEvent(JaxEvent.PLACE_ORDER_TRIGGER);*/
		JaxContextUtil.setRequestModel(placeOrder);
		ApiResponse response = null;
		response = placeOrderService.rateAlertPlaceOrder(fromAmount,toAmount,countryId,currencyId,bankId,derivedSellRate);
		
		return response;
	}
}
