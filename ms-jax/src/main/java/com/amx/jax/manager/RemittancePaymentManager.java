package com.amx.jax.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.RemittanceReceiptSubreport;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.PromotionDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.JaxTransactionStatus;
import com.amx.jax.dao.JaxEmployeeDao;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dao.RemittanceProcedureDao;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.PlaceOrder;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.partner.TransactionDetailsView;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.dbmodel.remittance.ShoppingCartDetails;
import com.amx.jax.error.JaxError;
import com.amx.jax.logger.AuditEvent.Result;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.events.CActivityEvent;
import com.amx.jax.logger.events.CActivityEvent.Type;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.jax.model.response.remittance.RemittanceResponseDto;
import com.amx.jax.model.response.serviceprovider.Remittance_Call_Response;
import com.amx.jax.partner.dao.PartnerTransactionDao;
import com.amx.jax.partner.dto.RemitTrnxSPDTO;
import com.amx.jax.partner.manager.PartnerTransactionManager;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.pricer.var.PricerServiceConstants.SERVICE_PROVIDER_BANK_CODE;
import com.amx.jax.repository.IPlaceOrderDao;
import com.amx.jax.repository.IShoppingCartDetailsDao;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.service.FinancialService;
import com.amx.jax.services.AbstractService;
import com.amx.jax.services.JaxEmailNotificationService;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.services.RemittanceApplicationService;
import com.amx.jax.services.ReportManagerService;
import com.amx.jax.services.TransactionHistroyService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.dao.ReferralDetailsDao;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.util.RoundUtil;
import com.amx.utils.JsonUtil;


@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class RemittancePaymentManager extends AbstractService{
	private static final Logger logger = LoggerFactory.getLogger(RemittancePaymentManager.class);
	
	@Autowired
	IShoppingCartDetailsDao shoppingCartApplDao;
	
	@Autowired
	FinancialService finanacialService;
	
	@Autowired
	RemittanceApplicationRepository applicationDao;
	
	@Autowired
	RemittanceApplicationService remittanceApplicationService;
	
	@Autowired
	RemittanceProcedureDao remittanceDao;
	
	@Autowired
	JaxNotificationService notificationService;
	
	@Autowired
	CustomerDao customerDao;
	
	@Autowired
	TransactionHistroyService transactionHistroyService;
	
	@Autowired
	private RemittanceApplicationDao remitAppDao;
	
	@Autowired
	private ReportManagerService reportManagerService;
	
	@Autowired
	PromotionManager promotionManager;
	
	@Autowired
	JaxEmployeeDao employeeDao;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PushNotifyClient pushNotifyClient;
	
    @Autowired
    IPlaceOrderDao placeOrderdao;
    
	@Autowired
	RemittanceManager remittanceManager;
	
	@Autowired
	JaxEmailNotificationService jaxEmailNotificationService;
	
	@Autowired
	JaxNotificationService jaxNotificationService;
	
	@Autowired
	DailyPromotionManager dailyPromotionManager;
	
	@Autowired
   	ReferralDetailsDao refDao;
	
	@Autowired
	PartnerTransactionManager partnerTransactionManager;
	
	@Autowired
	PartnerTransactionDao partnerTransactionDao;
	
	@Autowired
    AuditService auditService;
	
	public ApiResponse<PaymentResponseDto> paymentCapture(PaymentResponseDto paymentResponse) {
		ApiResponse response = null;
		logger.debug("paymment capture :{}", JsonUtil.toJson(paymentResponse));
		List<ShoppingCartDetails>  shoppingCartList = new ArrayList<>();
		UserFinancialYear userFinancialYear = finanacialService.getUserFinancialYear();
		List<RemittanceApplication> lstPayIdDetails =null;
		BigDecimal collectionFinanceYear = null;
		BigDecimal collectionDocumentNumber = null;
		BigDecimal collectionDocumentCode = null;
		String errorMsg = null;
		Map<String,Object> remitanceMap  = null;

		logger.info("Customer Id :"+paymentResponse.getCustomerId());
		logger.info("Result code :"+paymentResponse.getResultCode()+"\t Auth Code :"+paymentResponse.getAuth_appNo());
		logger.info("Application country Id :"+paymentResponse.getApplicationCountryId());
		logger.info("Company Id:"+paymentResponse.getCompanyId());
		logger.info("UDF 1:"+paymentResponse.getUdf1()+"\t UDF 2 :"+paymentResponse.getUdf2()+"\t UDF 3 :"+paymentResponse.getUdf3()+"\t UDF4 :"+paymentResponse.getUdf4());

		try {
			response = getBlackApiResponse();
			if(!StringUtils.isBlank(paymentResponse.getPaymentId()) && !StringUtils.isBlank(paymentResponse.getResultCode()) 
					&& (paymentResponse.getResultCode().equalsIgnoreCase(ConstantDocument.CAPTURED)|| paymentResponse.getResultCode().equalsIgnoreCase(ConstantDocument.APPROVED))) 
			{

				lstPayIdDetails = applicationDao.fetchRemitApplTrnxRecordsByCustomerPayId(paymentResponse.getUdf3(),new Customer(paymentResponse.getCustomerId()));
				RemittanceApplication remittanceApplication = lstPayIdDetails.get(0);
				validateAmountMismatch(remittanceApplication, paymentResponse);
				remittanceApplication.setIsactive(ConstantDocument.Yes);
				applicationDao.save(remittanceApplication);
				paymentResponse.setCompanyId(remittanceApplication.getFsCompanyMaster().getCompanyId());
				if (remittanceApplication.getResultCode() != null) {
					logger.info("Existing payment id found: {}", remittanceApplication.getPaymentId());
					return response;
				}
				remittanceApplicationService.updatePaymentDetails(lstPayIdDetails, paymentResponse);
				/** Calling stored procedure  insertRemittanceOnline **/
				remitanceMap = remittanceApplicationService.saveRemittance(paymentResponse);
				
//				/** Referral Code **/
//				List<RemittanceTransaction> remittanceList = remitAppDao.getOnlineRemittanceList(paymentResponse.getCustomerId());
//				logger.info("Remittance Count:" + remittanceList.size());
//				if(remittanceList.size() == 0) {
//					ReferralDetails referralDetails = refDao.getReferralByCustomerId(paymentResponse.getCustomerId());
//					referralDetails.setIsConsumed("Y");
//					refDao.updateReferralCode(referralDetails);
//					if (referralDetails.getRefferedByCustomerId() != null) {
//						PushMessage pushMessage = new PushMessage();
//						pushMessage.setSubject("Refer To Win!");
//						pushMessage.setMessage(
//								"Congraturlations! Your reference has done the first transaction on AMIEC App! You will get a chance to win from our awesome Referral Program! Keep sharing the links to as many contacts you can and win exciting prices on referral success!");
//						pushMessage.addToUser(referralDetails.getRefferedByCustomerId());
//						pushNotifyClient.send(pushMessage);
//					}
//					
//					if(referralDetails.getCustomerId() != null) {
//						PushMessage pushMessage = new PushMessage();
//						pushMessage.setSubject("Refer To Win!");
//						pushMessage.setMessage(
//								"Welcome to Al Mulla family! Win a chance to get exciting offers at Al Mulla Exchange by sharing the links to as many contacts as you can.");
//						pushMessage.addToUser(referralDetails.getCustomerId());
//						pushNotifyClient.send(pushMessage);	
//					}
//				}			
				
				errorMsg = (String)remitanceMap.get("P_ERROR_MESG");
				errorMsg= null;
				if(remitanceMap!=null && !remitanceMap.isEmpty() && StringUtils.isBlank(errorMsg)){

					collectionFinanceYear = (BigDecimal)remitanceMap.get("P_COLLECT_FINYR");
					collectionDocumentNumber = (BigDecimal)remitanceMap.get("P_COLLECTION_NO");
					collectionDocumentCode = (BigDecimal)remitanceMap.get("P_COLLECTION_DOCUMENT_CODE");
					errorMsg = (String)remitanceMap.get("P_ERROR_MESG");

					logger.info("EX_INSERT_REMITTANCE_ONLINE collectionFinanceYear : " + collectionFinanceYear);
					logger.info("collectionDocumentNumber : " + collectionDocumentNumber);
					logger.info("collectionDocumentCode : " + collectionDocumentCode);
					logger.info("EX_INSERT_REMITTANCE_ONLINE errorMsg : " + errorMsg);
					
					// service Provider api
					RemittanceResponseDto responseDto = new RemittanceResponseDto();
					responseDto.setCollectionDocumentFYear(collectionFinanceYear);
					responseDto.setCollectionDocumentNo(collectionDocumentNumber);
					responseDto.setCollectionDocumentCode(collectionDocumentCode);
					callingServiceProviderApi(responseDto,paymentResponse.getCustomerId());

					//Update remittance_transaction_id for place order method call
					if (lstPayIdDetails.get(0) != null)	{	
						updatePlaceOrderTransactionId(lstPayIdDetails.get(0),paymentResponse);
					} 	

					/** Calling stored procedure  to move remittance to old emos **/
					if(JaxUtil.isNullZeroBigDecimalCheck(collectionDocumentNumber)) {
						paymentResponse.setCollectionDocumentCode(collectionDocumentCode);
						paymentResponse.setCollectionDocumentNumber(collectionDocumentNumber);
						paymentResponse.setCollectionFinanceYear(collectionFinanceYear);
						remitanceMap = remittanceApplicationService.saveRemittancetoOldEmos(paymentResponse);
						errorMsg = (String) remitanceMap.get("P_ERROR_MESSAGE");
						paymentResponse.setErrorText(errorMsg);
						logger.info("EX_INSERT_EMOS_TRANSFER_LIVE :" + errorMsg);

						/** For Receipt Print **/

						//response.getData().getValues().add(paymentResponse);
						response.setResponseStatus(ResponseStatus.OK);
						//response.getData().setType("pg_remit_response");
					}
					try {
						RemittanceTransaction remittanceTransaction = remitAppDao.getRemittanceTransaction(
								lstPayIdDetails.get(0).getDocumentNo(),
								lstPayIdDetails.get(0).getDocumentFinancialyear());
						TransactionHistroyDTO trxnDto = transactionHistroyService.getTransactionHistoryDto(
								paymentResponse.getCustomerId(), remittanceTransaction.getDocumentFinanceYear(),
								remittanceTransaction.getDocumentNo());
						Customer customer = customerDao.getCustById(remittanceTransaction.getCustomerId().getCustomerId());
						setMetaInfo(trxnDto, paymentResponse);
						// promotion check not for amg employee
						if (!employeeDao.isAmgEmployee(customer.getIdentityInt())) {
							promotionManager.promotionWinnerCheck(remittanceTransaction.getDocumentNo(),
									remittanceTransaction.getDocumentFinanceYear());
						}
						PromotionDto promotDto = promotionManager.getPromotionDto(remittanceTransaction.getDocumentNo(),
								remittanceTransaction.getDocumentFinanceYear());
						PersonInfo personInfo = userService.getPersonInfo(customer.getCustomerId());
						if(personInfo!=null && !StringUtils.isBlank(personInfo.getEmail())) {
							promotionManager.sendVoucherEmail(promotDto, personInfo);
						}

						// --- WantIT BuyIT Coupons Promotions
						dailyPromotionManager.applyWantITbuyITCoupans(remittanceTransaction.getRemittanceTransactionId(), personInfo);
						logger.debug("Jolibee Padala");
						dailyPromotionManager.applyJolibeePadalaCoupons(remittanceTransaction.getDocumentFinanceYear(),remittanceTransaction.getDocumentNo(),remittanceTransaction.getBranchId().getBranchId());

						reportManagerService.generatePersonalRemittanceReceiptReportDetails(trxnDto, Boolean.TRUE);
						List<RemittanceReceiptSubreport> rrsrl = reportManagerService
								.getRemittanceReceiptSubreportList();
						PersonInfo personinfo = new PersonInfo();
						try {
							BeanUtils.copyProperties(personinfo, customer);
						} catch (Exception e) {
						}
						if(personInfo!=null && !StringUtils.isBlank(personInfo.getEmail())) {
							notificationService.sendTransactionNotification(rrsrl.get(0), personinfo);
						}
					} catch (Exception e) {
						logger.error("error while sending transaction notification", e);
					}

				}else {
					logger.info("PaymentResponseDto "+paymentResponse.getPaymentId()+"\t Result :"+paymentResponse.getResultCode()+"\t Custoemr Id :"+paymentResponse.getCustomerId());

					lstPayIdDetails =applicationDao.fetchRemitApplTrnxRecordsByCustomerPayId(paymentResponse.getPaymentId(),new Customer(paymentResponse.getCustomerId()));
					if(!lstPayIdDetails.isEmpty()) {
						paymentResponse.setErrorText(errorMsg);
						remittanceApplicationService.updatePayTokenNull(lstPayIdDetails, paymentResponse);
					}
				}


			}else{
				logger.info("PaymentResponseDto "+paymentResponse.getPaymentId()+"\t Result :"+paymentResponse.getResultCode()+"\t Custoemr Id :"+paymentResponse.getCustomerId());
				lstPayIdDetails =applicationDao.fetchRemitApplTrnxRecordsByCustomerPayId(paymentResponse.getUdf3(),new Customer(paymentResponse.getCustomerId()));
				if(!lstPayIdDetails.isEmpty()) {
					remittanceApplicationService.updatePayTokenNull(lstPayIdDetails, paymentResponse);
				}
				response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			}

		}catch(Exception e) {
			lstPayIdDetails =applicationDao.fetchRemitApplTrnxRecordsByCustomerPayId(paymentResponse.getUdf3(),new Customer(paymentResponse.getCustomerId()));
			
			if(!lstPayIdDetails.isEmpty()) {
				if (lstPayIdDetails.get(0).getResultCode() != null) {
					logger.info("Existing payment id found: {}", lstPayIdDetails.get(0).getPaymentId());
					return response;
				}
				remittanceApplicationService.updatePayTokenNull(lstPayIdDetails, paymentResponse);
				
			}

			throw new GlobalException(JaxError.PG_ERROR,"Remittance error :"+errorMsg);
		}
		response.getData().getValues().add(paymentResponse);
		response.getData().setType("pg_remit_response");
		checkAndSendAlertEmail(errorMsg, paymentResponse);
		return response;
	}

	@Async(ExecutorConfig.DEFAULT)
	private void checkAndSendAlertEmail(String errorMsg, PaymentResponseDto paymentResponse) {
		String[] receiverList = jaxEmailNotificationService.getBeneCreationErrorEmailList();
		if (StringUtils.isNotBlank(errorMsg) && receiverList != null && receiverList.length > 0) {
			StringBuffer message = new StringBuffer();
			message.append("errorMsg: ").append(errorMsg);
			message.append("paymentResponse: ").append(paymentResponse);
			Email email = new Email();
			email.setSubject("Remittance creation failure");
			email.addTo(receiverList);
			email.setMessage(message.toString());
			notificationService.sendEmail(email);
		}
	}

	private void setMetaInfo(TransactionHistroyDTO transactionHistroyDTO, PaymentResponseDto paymentResponse) {
		transactionHistroyDTO.setApplicationCountryId(paymentResponse.getApplicationCountryId());
		transactionHistroyDTO.setCompanyId(paymentResponse.getCompanyId());
		transactionHistroyDTO.setLanguageId(new BigDecimal(1));
		transactionHistroyDTO.setCustomerId(paymentResponse.getCustomerId());		
	}

	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ApiResponse savePaymentId(PaymentResponseDto paymentResponse) {
		ApiResponse response = getBlackApiResponse();
		logger.info("in savePaymentId  :" + paymentResponse.toString());
		List<RemittanceApplication> lstPayIdDetails = applicationDao.fetchRemitApplTrnxRecordsByCustomerPayId(
				paymentResponse.getUdf3(), new Customer(paymentResponse.getCustomerId()));
		if (lstPayIdDetails == null || lstPayIdDetails.isEmpty()) {
			throw new GlobalException("No Application data found for given payment id: " + paymentResponse.getUdf3());
		}
		lstPayIdDetails.get(0).setPaymentId(paymentResponse.getPaymentId());
		applicationDao.save(lstPayIdDetails.get(0));
		response.getData().getValues().add(paymentResponse);
		response.getData().setType("pg_remit_response");
		return response;
	}
	
	//Update remittance_transaction_id for place order
    public void updatePlaceOrderTransactionId(RemittanceApplication remittanceApplication,PaymentResponseDto paymentResponse) {
    	List<PlaceOrder> poList = placeOrderdao.getPlaceOrderForRemittanceApplicationId(remittanceApplication.getRemittanceApplicationId());
    	PlaceOrder po =null;
    	if (poList!=null && poList.size()!=0) {
    		po=poList.get(0);
        	logger.info(String.format("Place Order id : %s found for remittance_application_id : %s ", po.getOnlinePlaceOrderId(),remittanceApplication.getRemittanceApplicationId()));
        	po.setRemittanceTransactionId(new BigDecimal(paymentResponse.getTransactionId()));
        	po.setIsActive("C");
        	placeOrderdao.save(po);
    	}else {
    		logger.info("Place Order not found for remittance_application_id: " + remittanceApplication.getRemittanceApplicationId());
    	}
       
    }

	private void validateAmountMismatch(RemittanceApplication remittanceApplication, PaymentResponseDto paymentResponse) {
		BigDecimal localNetTraxAmount = remittanceApplication.getLocalNetTranxAmount();
		BigDecimal loyalityPointEncashed = remittanceApplication.getLoyaltyPointsEncashed();
		loyalityPointEncashed = (loyalityPointEncashed == null ? BigDecimal.ZERO : loyalityPointEncashed);
		BigDecimal localCurrencyDecimalNumber = remittanceApplication.getExCurrencyMasterByLocalChargeCurrencyId().getDecinalNumber();
		BigDecimal payableAmount = localNetTraxAmount.subtract(loyalityPointEncashed);
		if (paymentResponse.getAmount() == null) {
			logger.info("amount null in paymentResponse");
		}
		BigDecimal paidAmount = new BigDecimal(paymentResponse.getAmount());
		payableAmount = RoundUtil.roundBigDecimal(payableAmount, localCurrencyDecimalNumber.intValue());
		paidAmount = RoundUtil.roundBigDecimal(paidAmount, localCurrencyDecimalNumber.intValue());
		if (!paidAmount.equals(payableAmount)) {
			String errorMessage = String.format("paidAmount: %s and payableAmount: %s mismatch for remittanceApplicationId: %s", paidAmount,
					payableAmount, remittanceApplication.getRemittanceApplicationId());
			logger.info(errorMessage);
			jaxNotificationService.sendTransactionErrorAlertEmail(errorMessage, "Remittance Amount mistmatch", paymentResponse);
			throw new GlobalException("paid and payable amount mismatch");
		}
	}
	
	public void callingServiceProviderApi(RemittanceResponseDto responseDto,BigDecimal customerId) {
		TransactionDetailsView serviceProviderView = null;
		String partnerTransactionId = null;
		
		if(responseDto!=null && JaxUtil.isNullZeroBigDecimalCheck(responseDto.getCollectionDocumentNo())) {
			Boolean spCheckStatus = Boolean.FALSE;
			List<TransactionDetailsView> lstTrnxDetails = partnerTransactionDao.fetchTrnxSPDetails(customerId,responseDto.getCollectionDocumentFYear(),responseDto.getCollectionDocumentNo());
			for (TransactionDetailsView transactionDetailsView : lstTrnxDetails) {
				if(transactionDetailsView.getBankCode().equalsIgnoreCase(SERVICE_PROVIDER_BANK_CODE.HOME.name())) {
					spCheckStatus = Boolean.TRUE;
					serviceProviderView = transactionDetailsView;
					break;
				}
			}
			
			if(spCheckStatus) {
				AmxApiResponse<Remittance_Call_Response, Object> apiResponse = partnerTransactionManager.callingPartnerApi(responseDto);
				if(apiResponse != null) {
					if(serviceProviderView != null && serviceProviderView.getPartnerSessionId() != null) {
						partnerTransactionId = serviceProviderView.getPartnerSessionId();
					}
					RemitTrnxSPDTO remitTrnxSPDTO = partnerTransactionManager.saveRemitTransactionDetails(apiResponse,responseDto,partnerTransactionId);
					if(remitTrnxSPDTO != null && remitTrnxSPDTO.getActionInd() != null && remitTrnxSPDTO.getResponseDescription() != null) {
						// got success to fetch response from API
						logger.info(" Service provider result Action Ind " +remitTrnxSPDTO.getActionInd() + " Description : " + remitTrnxSPDTO.getResponseDescription());
					}else {
						logger.debug("Service provider api fail to execute : ColDocNo : ", responseDto.getCollectionDocumentNo() + " : ColDocCod : " +responseDto.getCollectionDocumentCode()+"  : ColDocYear : "+responseDto.getCollectionDocumentFYear());
						auditService.log(new CActivityEvent(Type.TRANSACTION_CREATED,String.format("%s/%s", responseDto.getCollectionDocumentFYear(),responseDto.getCollectionDocumentNo())).field("STATUS").to(JaxTransactionStatus.PAYMENT_SUCCESS_SERVICE_PROVIDER_FAIL).result(Result.DONE));
						throw new GlobalException("Transaction failed to send to Service Provider");
					}
				}else {
					logger.debug("Service provider api fail to execute : ColDocNo : ", responseDto.getCollectionDocumentNo() + " : ColDocCod : " +responseDto.getCollectionDocumentCode()+"  : ColDocYear : "+responseDto.getCollectionDocumentFYear());
					auditService.log(new CActivityEvent(Type.TRANSACTION_CREATED,String.format("%s/%s", responseDto.getCollectionDocumentFYear(),responseDto.getCollectionDocumentNo())).field("STATUS").to(JaxTransactionStatus.PAYMENT_SUCCESS_SERVICE_PROVIDER_FAIL).result(Result.DONE));
					throw new GlobalException("Transaction failed to send to Service Provider");
				}
			}
		}else {
			logger.error("Service provider api fail to execute : ColDocNo : ", responseDto.getCollectionDocumentNo() + " : ColDocCod : " +responseDto.getCollectionDocumentCode()+"  : ColDocYear : "+responseDto.getCollectionDocumentFYear());
		}
	}
}
