package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.REMIT_API_ENDPOINT;

import java.math.BigDecimal;

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
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.meta.MetaData;
import com.amx.jax.services.PurposeOfTransactionService;
import com.amx.jax.services.RemittanceTransactionService;
import com.amx.jax.services.ReportManagerService;
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

	@Autowired
	RemittanceTransactionService remittanceTransactionService;
	
	@Autowired
	PurposeOfTransactionService purposeOfTransactionService;

	@Autowired
	ReportManagerService reportManagerService;

	@Autowired
	HttpServletResponse httpServletResponse;
	
	@Autowired
	MetaData metaData;
	

	@RequestMapping(value = "/trnxHist/", method = RequestMethod.GET)
	public ApiResponse getTrnxHistroyDetailResponse(@RequestParam("docfyr") BigDecimal docfyr, @RequestParam("docNumber") String docNumber,
			@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate) {
		
		
		BigDecimal customerId = metaData.getCustomerId();
		
		logger.info("customerId :" + customerId + "\t docfyr :" + docfyr + "\t docNumber :" + docNumber
				+ "\t fromDate :" + fromDate + "\t toDate :" + toDate);
		ApiResponse response = null;
		
		
		if (docNumber!=null && !docNumber.equals("null")) {
			response = transactionHistroyService.getTransactionHistroyByDocumentNumber(customerId, docfyr,
					new BigDecimal(docNumber));
		} else if ((fromDate !=null && !fromDate.equals("0") && !fromDate.equals("null"))||(toDate !=null && !toDate.equals("0") && !toDate.equals("null"))) {
			response = transactionHistroyService.getTransactionHistroyDateWise(customerId, docfyr, fromDate, toDate);
		} else {
			response = transactionHistroyService.getTransactionHistroy(customerId, docfyr);
		}
		return response;
	}

	@RequestMapping(value = "/remitReport/", method = RequestMethod.POST)
	public ApiResponse getRemittanceDetailForReport(@RequestBody String jsonTransactionHistroyDTO) {
		logger.info("getRemittanceDetailForReport Trnx Report:");
		TransactionHistroyDTO transactionHistroyDTO = (TransactionHistroyDTO) converterUtil
				.unmarshall(jsonTransactionHistroyDTO, TransactionHistroyDTO.class);
		logger.info("Colle Doc No :" + transactionHistroyDTO.getCollectionDocumentNo());
		logger.info("Colle Doc code :" + transactionHistroyDTO.getCollectionDocumentCode());
		logger.info("Colle Doc Fyear :" + transactionHistroyDTO.getCollectionDocumentFinYear());
		logger.info("Customer Id :" + transactionHistroyDTO.getCustomerId() + "\t Reference :"
				+ transactionHistroyDTO.getCustomerReference());
		logger.info("Country Id :" + transactionHistroyDTO.getApplicationCountryId() + "\t Currency Id :"
				+ transactionHistroyDTO.getCurrencyId());

		ApiResponse response = reportManagerService
				.generatePersonalRemittanceReceiptReportDetails(transactionHistroyDTO);
		return response;
	}

	@RequestMapping(value = "/remitPrint/{documnetNo}/{docFyr}/", method = RequestMethod.GET)
	public ApiResponse getRemittanceDetailForPrintResponseTest(@PathVariable("documnetNo") BigDecimal documnetNo,
			@PathVariable("docFyr") BigDecimal docFyr) {
		ApiResponse response = remittanceTransactionService.getRemittanceTransactionDetails(documnetNo, docFyr,
				ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION);
		return response;
	}

	
	@RequestMapping(value = "/validate/", method = RequestMethod.POST)
	public ApiResponse validateRemittanceTransaction(RemittanceTransactionRequestModel model) {
		ApiResponse response = remittanceTransactionService.validateRemittanceTransaction(model);
		return response;
	}
	
	@RequestMapping(value = "/sourceofincome/", method = RequestMethod.POST)
	public ApiResponse sourceofIncome() {
		BigDecimal languageId=metaData.getLanguageId();
		if(languageId==null || languageId.compareTo(BigDecimal.ZERO)==0) {
			languageId=new BigDecimal(1);
		}
		ApiResponse response = remittanceTransactionService.getSourceOfIncome(languageId);
		return response;
	}

	@RequestMapping(value = "/save-application/", method = RequestMethod.POST)
	public ApiResponse saveApplication(RemittanceTransactionRequestModel model) {
		logger.info("In Save-Application with parameters" + model.toString());
		ApiResponse response = remittanceTransactionService.saveApplication(model);
		return response;
	}

	@RequestMapping(value = "/purpose-of-txn/list/", method = RequestMethod.POST)
	public ApiResponse getPurposeOfTransaction( RemittanceTransactionRequestModel model) {
		logger.info("In getPurposeOfTransaction with parameters" + model.toString());
		ApiResponse response = purposeOfTransactionService.getPurposeOfTransaction(model);
		return response;
	}
	
}
