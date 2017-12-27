package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.RATE_ALERT_ENDPOINT;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.RateAlertDTO;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.meta.MetaData;
import com.amx.jax.services.RateAlertService;
import com.amx.jax.services.RemittanceTransactionService;
import com.amx.jax.services.ReportManagerService;
import com.amx.jax.services.TransactionHistroyService;
import com.amx.jax.util.ConverterUtil;

@RestController
@RequestMapping(RATE_ALERT_ENDPOINT)
@SuppressWarnings("rawtypes")
public class RateAlertController {

	private Logger logger = Logger.getLogger(RateAlertController.class);



	@Autowired
	RateAlertService rateAlertService;

	@Autowired
	MetaData metaData;
	
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ApiResponse handleUrlSave(@RequestBody RateAlertDTO dto) {
		logger.info("In save with parameters" + dto.toString());
		
		ApiResponse response = null;
		response = rateAlertService.saveRateAlert(dto);
		return response;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ApiResponse handleUrlDelete(@RequestBody RateAlertDTO dto) {
		logger.info("In delete with parameters" + dto.toString());
		
		ApiResponse response = null;
		response = rateAlertService.delteRateAlert(dto);
		return response;
	}
	
	@RequestMapping(value = "/get/for/customer", method = RequestMethod.POST)
	public ApiResponse handleUrlGetRateAlertForCustomer() {
		BigDecimal customerId =metaData.getCustomerId();
		logger.info("In /get/for/customer with customerId :" + customerId );
		
		ApiResponse response = null;
		response = rateAlertService.getRateAlertForCustomer(customerId);
		return response;
	}
	
//	@RequestMapping(value = "/save", method = RequestMethod.GET)
//	public ApiResponse handleUrlSaveRateAlert(@RequestParam("customerId") BigDecimal customerId,
//											  @RequestParam("fccur") BigDecimal fccur, 
//											  @RequestParam("basecur") BigDecimal basecur,
//											  @RequestParam("fromDate") Date fromDate, 
//											  @RequestParam("toDate") Date toDate,
//											  @RequestParam("alertRate") BigDecimal alertRate,
//											  @RequestParam("rule") String rule) {
//		
//		Map<String,Object> paramMap = new HashMap<String,Object>();
//		paramMap.put("customerId", customerId);
//		paramMap.put("basecur", basecur);
//		paramMap.put("fccur",fccur);
//		paramMap.put("fromDate", fromDate);
//		paramMap.put("toDate", toDate);
//		paramMap.put("alertRate", alertRate);
//		paramMap.put("rule", rule);
//		
//		logger.info("Inside /save for customer : "+customerId);
//		ApiResponse response = null;
//		response = rateAlertService.saveRateAlert(paramMap);
//		return response;
//	}
	
//	@RequestMapping(value = "/get/for/customer", method = RequestMethod.GET)
//	public ApiResponse handleUrlGetRateAlertForCustomer(@RequestParam("customerId") String customerId) {
//		
//		Map<String,Object> paramMap = new HashMap<String,Object>();
//		paramMap.put("customerId", customerId);
//		
//		logger.info("customerId :" + customerId );
//		ApiResponse response = null;
//		response = rateAlertService.getRateAlertForCustomer(paramMap);
//		return response;
//	}
	
/*	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ApiResponse handleUrlDeleteRateAlert(@RequestParam("rateAlertId") BigDecimal onlineRateAlertId) {
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("alertId", onlineRateAlertId);
		
		logger.info("Delete alert of alertId :" + onlineRateAlertId );
		ApiResponse response = null;
		response = rateAlertService.delteRateAlert(paramMap);
		return response;
	}*/


}
