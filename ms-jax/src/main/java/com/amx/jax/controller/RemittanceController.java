package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.REMIT_API_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.services.TransactionHistroyService;
import com.amx.jax.util.ConverterUtil;

@RestController
@RequestMapping(REMIT_API_ENDPOINT)
@SuppressWarnings("rawtypes")
public class RemittanceController {
	
	private Logger logger = Logger.getLogger(RemittanceController.class);
	
	
	@Autowired
	private ConverterUtil converterUtil;
	
	@Autowired
	TransactionHistroyService transactionHistroyService;
	
	@RequestMapping(value = "/trnxHist/", method = RequestMethod.GET)
	public ApiResponse getTrnxHistroyDetailResponse(@RequestParam("customerId") BigDecimal customerId,
			@RequestParam("docfyr") BigDecimal docfyr,
			@RequestParam("docNumber") String docNumber,
			@RequestParam("fromDate")  String fromDate,
			@RequestParam("toDate") String toDate) {	
			
		logger.info("customerId :"+customerId+"\t docfyr :"+docfyr+"\t docNumber :"+docNumber+"\t fromDate :"+fromDate+"\t toDate :"+toDate);
		ApiResponse response = null;
		if(docNumber!=null && !docNumber.equals("null")) {
			 response = transactionHistroyService.getTransactionHistroyByDocumentNumber(customerId, docfyr, new BigDecimal(docNumber));
		}else if((fromDate!= null && !fromDate.equals("null")) || (toDate!= null && !toDate.equals("null"))) {
			response = transactionHistroyService.getTransactionHistroyDateWise(customerId, docfyr,fromDate,toDate); 
		}
		else {
			response = transactionHistroyService.getTransactionHistroy(customerId, docfyr); //, fromDate, toDate
		}
		return response;
	}
	
	

}
