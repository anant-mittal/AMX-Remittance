package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.REMIT_API_ENDPOINT;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.request.RemittanceTransactionStatusRequestModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.RemittanceTransactionStatusResponseModel;
import com.amx.jax.AmxMeta;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.fx.IFxBranchOrderService.Params;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.JaxEvent;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerRating;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.dict.AmxEnums;
import com.amx.jax.dict.Language;
import com.amx.jax.manager.RemittancePaymentManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.CustomerRatingDTO;
import com.amx.jax.model.request.remittance.BranchRemittanceRequestModel;
import com.amx.jax.model.request.remittance.IRemitTransReqPurpose;
import com.amx.jax.model.request.remittance.RemittanceTransactionDrRequestModel;
import com.amx.jax.model.request.remittance.RemittanceTransactionRequestModel;
import com.amx.jax.model.response.SourceOfIncomeDto;
import com.amx.jax.model.response.remittance.BranchRemittanceApplResponseDto;
import com.amx.jax.model.response.remittance.RemittanceApplicationResponseModel;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.services.CustomerRatingService;
import com.amx.jax.services.PurposeOfTransactionService;
import com.amx.jax.services.RemittanceTransactionService;
import com.amx.jax.services.ReportManagerService;
import com.amx.jax.services.TransactionHistroyService;
import com.amx.jax.userservice.dao.ReferralDetailsDao;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.ConverterUtil;
import com.amx.jax.util.JaxContextUtil;

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
	RemittancePaymentManager remittancePaymentManager;

	@Autowired
	HttpServletResponse httpServletResponse;

	@Autowired
	MetaData metaData;

	@Autowired
	RemittanceApplicationDao remitAppDao;

	@Autowired
	CustomerRatingService customerRatingService;

	@Autowired
	UserService userService;

	@Autowired
	ReferralDetailsDao refDao;

	@Autowired
	PushNotifyClient pushNotifyClient;

	@Autowired
	protected AmxMeta amxMeta;

	@RequestMapping(value = "/trnxHist/", method = RequestMethod.GET)
	public ApiResponse getTrnxHistroyDetailResponse(@RequestParam(required = false, value = "docfyr") BigDecimal docfyr,
			@RequestParam(required = false, value = "docNumber") String docNumber,
			@RequestParam(required = false, value = "fromDate") String fromDate,
			@RequestParam(required = false, value = "toDate") String toDate) {

		BigDecimal customerId = metaData.getCustomerId();

		logger.info("customerId :" + customerId + "\t docfyr :" + docfyr + "\t docNumber :" + docNumber
				+ "\t fromDate :" + fromDate + "\t toDate :" + toDate);
		ApiResponse response = null;

		if (docNumber != null && !docNumber.equals("null")) {
			response = transactionHistroyService.getTransactionHistroyByDocumentNumber(customerId, docfyr,
					new BigDecimal(docNumber));
		} else if ((fromDate != null && !fromDate.equals("0") && !fromDate.equals("null"))
				|| (toDate != null && !toDate.equals("0") && !toDate.equals("null"))) {
			response = transactionHistroyService.getTransactionHistroyDateWise(customerId, docfyr, fromDate, toDate);
		} else {
			response = transactionHistroyService.getTransactionHistroy(customerId, docfyr);
		}
		return response;
	}

	@RequestMapping(value = "/remitReport/", method = RequestMethod.POST)
	public ApiResponse getRemittanceDetailForReport(@RequestBody String jsonTransactionHistroyDTO,
			@RequestParam("promotion") Boolean promotion) {
		logger.info("getRemittanceDetailForReport Trnx Report:");
		logger.debug("getRemittanceDetailForReport Trnx Report:");
		logger.debug("Json tras history is " + jsonTransactionHistroyDTO);
		logger.debug("promotion value is " + promotion);
		TransactionHistroyDTO transactionHistroyDTO = (TransactionHistroyDTO) converterUtil
				.unmarshall(jsonTransactionHistroyDTO, TransactionHistroyDTO.class);
		logger.info("Colle Doc No :" + transactionHistroyDTO.getCollectionDocumentNo());
		logger.info("Colle Doc code :" + transactionHistroyDTO.getCollectionDocumentCode());
		logger.info("Colle Doc Fyear :" + transactionHistroyDTO.getCollectionDocumentFinYear());
		logger.info("Customer Id :" + transactionHistroyDTO.getCustomerId() + "\t Reference :"
				+ transactionHistroyDTO.getCustomerReference());
		logger.info("Country Id :" + transactionHistroyDTO.getApplicationCountryId() + "\t Currency Id :"
				+ transactionHistroyDTO.getCurrencyId());

		transactionHistroyDTO.setCompanyId(metaData.getCompanyId());
		ApiResponse response = reportManagerService
				.generatePersonalRemittanceReceiptReportDetails(transactionHistroyDTO, Boolean.TRUE);
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
	public ApiResponse validateRemittanceTransaction(@RequestBody RemittanceTransactionRequestModel model) {
		logger.info("In validate with parameters" + model.toString());
		ApiResponse response = remittanceTransactionService.validateRemittanceTransaction(model);
		return response;
	}

	@RequestMapping(value = "/sourceofincome/", method = RequestMethod.POST)
	public AmxApiResponse<SourceOfIncomeDto, Object> sourceofIncome() {
		BigDecimal languageId = amxMeta.getClientLanguage(Language.EN).getBDCode();
		logger.debug("sourceofIncome lng " + languageId);
		return remittanceTransactionService.getSourceOfIncome(languageId);
	}

	@RequestMapping(value = "/save-application/", method = RequestMethod.POST)
	public ApiResponse saveApplication(@RequestBody @Valid RemittanceTransactionRequestModel model) {
		JaxContextUtil.setJaxEvent(JaxEvent.CREATE_APPLICATION);
		JaxContextUtil.setRequestModel(model);
		logger.info("In Save-Application with parameters" + model.toString());
		ApiResponse response = remittanceTransactionService.saveApplication(model);
		return response;
	}

	@RequestMapping(value = "/save-application/v2/", method = RequestMethod.POST)
	public ApiResponse saveApplication(@RequestBody @Valid RemittanceTransactionDrRequestModel model) {
		JaxContextUtil.setJaxEvent(JaxEvent.CREATE_APPLICATION);
		JaxContextUtil.setRequestModel(model);
		logger.info("In Save-Application with parameters" + model.toString());
		ApiResponse response = remittanceTransactionService.saveApplicationV2(model);
		return response;
	}

	@RequestMapping(value = "/purpose-of-txn/list/", method = RequestMethod.POST)
	public ApiResponse getPurposeOfTransaction(@RequestBody IRemitTransReqPurpose model) {
		logger.info("In getPurposeOfTransaction with parameters" + model.toString());
		ApiResponse response = purposeOfTransactionService.getPurposeOfTransaction(model);
		return response;
	}

	@RequestMapping(value = "/save-remittance/", method = RequestMethod.POST)
	public ApiResponse saveRemittance(@RequestBody PaymentResponseDto paymentResponse) {
		JaxContextUtil.setJaxEvent(JaxEvent.CREATE_REMITTANCE);
		JaxContextUtil.setRequestModel(paymentResponse);
		logger.info("save-Remittance Controller :" + paymentResponse.getCustomerId() + "\t country ID :");
		logger.debug("Payment respone is " + paymentResponse.toString());
		logger.debug("save-Remittance Controller :" + paymentResponse.getCustomerId() + "\t country ID :"
				+ paymentResponse.getApplicationCountryId() + "\t Compa Id:" + paymentResponse.getCompanyId());

		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal applicationCountryId = metaData.getCountryId();
		BigDecimal companyId = metaData.getCompanyId();
		if (customerId != null) {
			paymentResponse.setCustomerId(customerId);
		} else {
			paymentResponse.setCustomerId(new BigDecimal(paymentResponse.getTrackId()));
		}
		paymentResponse.setApplicationCountryId(applicationCountryId);
		paymentResponse.setCompanyId(companyId);
		logger.info("save-Remittance before payment capture :" + customerId + "\t country ID :" + applicationCountryId
				+ "\t Compa Id:" + companyId);

		// Referral
		List<RemittanceTransaction> remittanceList = remitAppDao.getOnlineRemittanceList(customerId);
		logger.info("Remittance Count:" + remittanceList.size());
////		if(remittanceList.size() == 0) {
//			ReferralDetails referralDetails = refDao.getReferralByCustomerId(customerId);
//			referralDetails.setIsConsumed("Y");
//			refDao.updateReferralCode(referralDetails);
//			if (referralDetails.getRefferedByCustomerId() != null) {
//				PushMessage pushMessage = new PushMessage();
//				pushMessage.setSubject("Refer To Win!");
//				pushMessage.setMessage(
//						"Congraturlations! Your reference has done the first transaction on AMIEC App! You will get a chance to win from our awesome Referral Program! Keep sharing the links to as many contacts you can and win exciting prices on referral success!");
//				pushMessage.addToUser(referralDetails.getRefferedByCustomerId());
//				pushNotifyClient.send(pushMessage);
//			}
//			
//			if(referralDetails.getCustomerId() != null) {
//				PushMessage pushMessage = new PushMessage();
//				pushMessage.setSubject("Refer To Win!");
//				pushMessage.setMessage(
//						"Welcome to Al Mulla family! Win a chance to get exciting offers at Al Mulla Exchange by sharing the links to as many contacts as you can.");
//				pushMessage.addToUser(referralDetails.getCustomerId());
//				pushNotifyClient.send(pushMessage);	
//			}
////		}	
		ApiResponse response = null;
		if(paymentResponse!=null && paymentResponse.getProduct().equals(AmxEnums.Products.REMIT_SINGLE)) { /** for compatability **/
			response = remittancePaymentManager.paymentCapture(paymentResponse);
		}else {
			response = remittancePaymentManager.paymentCaptureV2(paymentResponse);
		}
		return response;
	}

	
	
	
	
	
	
	@RequestMapping(value = "/status/", method = RequestMethod.POST)
	public ApiResponse getTransactionStatus(@RequestBody RemittanceTransactionStatusRequestModel request,
			@RequestParam("promotion") Boolean promotion) {

		logger.info("In getTransactionStatus with param, :  " + request.toString());
		request.setPromotion(promotion);
		ApiResponse response = remittanceTransactionService.getTransactionStatus(request);
		return response;
	}

	@RequestMapping(value = "/save-payment-id/", method = RequestMethod.POST)
	public ApiResponse savePaymentId(@RequestBody PaymentResponseDto paymentResponse) {
		logger.info("save-Remittance Controller :" + paymentResponse.getCustomerId() + "\t country ID :"
				+ paymentResponse.getApplicationCountryId() + "\t Compa Id:" + paymentResponse.getCompanyId());

		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal applicationCountryId = metaData.getCountryId();
		BigDecimal companyId = metaData.getCompanyId();
		if (customerId != null) {
			paymentResponse.setCustomerId(customerId);
		} else {
			paymentResponse.setCustomerId(new BigDecimal(paymentResponse.getTrackId()));
		}
		paymentResponse.setApplicationCountryId(applicationCountryId);
		paymentResponse.setCompanyId(companyId);
		logger.info(String.format("save-payment-id : for customer - %s, payment_id - %s", paymentResponse.getTrackId(),
				paymentResponse.getPaymentId()));

		ApiResponse response = remittancePaymentManager.savePaymentId(paymentResponse);
		return response;
	}

	@RequestMapping(value = "/trnx/receipt", method = RequestMethod.POST)
	public ApiResponse getReceiptJson(@RequestParam BigDecimal appDocNo, @RequestParam BigDecimal appDocFinYear) {
		RemittanceTransaction remittanceTransaction = remitAppDao.getRemittanceTransaction(appDocNo, appDocFinYear);
		Customer customer = userService.getCustById(metaData.getCustomerId());
		if (remittanceTransaction.getCustomerId() != null) {
			customer = remittanceTransaction.getCustomerId();
		}
		BigDecimal cutomerReference = customer.getCustomerId();
		BigDecimal remittancedocfyr = remittanceTransaction.getDocumentFinanceYear();
		BigDecimal remittancedocNumber = remittanceTransaction.getDocumentNo();

		TransactionHistroyDTO transactionHistoryDto = transactionHistroyService.getTransactionHistoryDto(
				cutomerReference, remittancedocfyr,
				remittancedocNumber);
		transactionHistoryDto.setApplicationCountryId(metaData.getCountryId());
		return reportManagerService.generatePersonalRemittanceReceiptReportDetails(transactionHistoryDto, true);
	}

	@RequestMapping(value = "/calc/", method = RequestMethod.POST)
	public ApiResponse calcEquivalentAmount(@RequestBody RemittanceTransactionRequestModel model) {
		logger.info("In calcEquivalentAmount with parameters" + model.toString());
		ApiResponse response = remittanceTransactionService.calcEquivalentAmount(model);
		return response;
	}

	@RequestMapping(value = "/save-customer-rating/", method = RequestMethod.POST)
	public AmxApiResponse<CustomerRating, ?> saveCustomerRating(@RequestBody @Valid CustomerRatingDTO dto) {
		return customerRatingService.saveCustomerRating(dto);
	}

	// radhika
	@RequestMapping(value = "/customer-trnx-rating/", method = RequestMethod.POST)
	public AmxApiResponse<CustomerRating, ?> inquireCustomerRating(@RequestParam BigDecimal remittanceTrnxId,
			@RequestParam(value = Params.FX_PRODUCT) String product) {
		return customerRatingService.inquireCustomerRating(remittanceTrnxId, product);

	}

	/** added by Rabil **/
	@RequestMapping(value = "/pay-shopping-cart/", method = RequestMethod.POST)
	public AmxApiResponse<RemittanceApplicationResponseModel,Object> payShoppingCart(@RequestBody @Valid BranchRemittanceRequestModel remittanceRequestModel) {
		RemittanceApplicationResponseModel response = remittancePaymentManager.payShoppingCart(remittanceRequestModel);
		return AmxApiResponse.build(response);
	}
	
	
	/** added by Rabil **/
	@RequestMapping(value = "/add-to-cart/", method = RequestMethod.POST)
	public AmxApiResponse<BranchRemittanceApplResponseDto, Object> addtoCart(@RequestBody @Valid RemittanceTransactionDrRequestModel model) {
		JaxContextUtil.setJaxEvent(JaxEvent.CREATE_APPLICATION);
		JaxContextUtil.setRequestModel(model);
		logger.info("In Save-Application with parameters" + model.toString());
		BranchRemittanceApplResponseDto response = remittanceTransactionService.addtoCart(model);
		return AmxApiResponse.build(response);
	}
	
	/** added by Rabil **/ 
	@RequestMapping(value = "/status/v2/", method = RequestMethod.POST)
	public AmxApiResponse<RemittanceTransactionStatusResponseModel, Object> getTransactionStatusV2(@RequestBody RemittanceTransactionStatusRequestModel request,@RequestParam("promotion") Boolean promotion) {
		logger.info("In getTransactionStatus with param, :  " + request.toString());
		request.setPromotion(promotion);
		return remittanceTransactionService.getTransactionStatusV2(request);
	}
}
