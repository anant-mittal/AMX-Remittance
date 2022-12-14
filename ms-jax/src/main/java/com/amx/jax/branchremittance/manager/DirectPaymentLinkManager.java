package com.amx.jax.branchremittance.manager;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.AmxConfig;
import com.amx.jax.api.ResponseCodeDetailDTO;
import com.amx.jax.config.JaxTenantProperties;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.JaxTransactionStatus;
import com.amx.jax.dao.BranchRemittancePaymentDao;
import com.amx.jax.dao.CustomerCartDao;
import com.amx.jax.dao.FcSaleApplicationDao;
import com.amx.jax.dbmodel.CustomerCartMaster;
import com.amx.jax.dbmodel.PaygDetailsModel;
import com.amx.jax.dbmodel.PaymentModeModel;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dict.PayGRespCodeJSONConverter;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.request.remittance.BranchApplicationDto;
import com.amx.jax.model.request.remittance.BranchRemittanceRequestModel;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.jax.model.response.remittance.BranchRemittanceApplResponseDto;
import com.amx.jax.model.response.remittance.CustomerShoppingCartDto;
import com.amx.jax.model.response.remittance.PaymentLinkRespDTO;
import com.amx.jax.model.response.remittance.PaymentLinkRespStatus;
import com.amx.jax.model.response.remittance.RemittanceCollectionDto;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.repository.CurrencyRepository;
import com.amx.jax.repository.PaygDetailsRepository;
import com.amx.jax.repository.PaymentModeRepository;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.repository.remittance.CustomerCartRepository;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.userservice.service.UserService;
import com.amx.utils.Random;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class DirectPaymentLinkManager extends AbstractModel {

	Logger logger = LoggerFactory.getLogger(getClass());
	private static final long serialVersionUID = 1L;

	@Autowired
	BranchRemittancePaymentDao branchRemittancePaymentDao;
	
	@Autowired
	RemittanceApplicationRepository remittanceApplicationRepository;
	
	@Autowired
	CurrencyRepository currencyRepository;
	
	@Autowired
	BranchRemittancePaymentManager branchRemittancePaymentManager;
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	FcSaleApplicationDao fcSaleApplicationDao;
	
	@Autowired
	PaygDetailsRepository pgRepository;
	
	@Autowired
	AmxConfig amxConfig;
	
	@Autowired
	BranchRemittanceSaveManager branchRemittanceSaveManager;
	
	@Autowired
	PaymentModeRepository payModeRepositoy;
	
	@Autowired
	UserService userService;
	
	@Autowired
	JaxNotificationService notificationService;
	
	@Autowired
	CustomerCartDao customerCartDao;
	
	@Autowired
	JaxTenantProperties jaxTenantProperties;
	
	@Autowired
	CustomerCartRepository customerCartRepository;
	
	public PaymentLinkRespDTO getPaymentLinkDetails(BigDecimal customerId, BranchRemittanceApplResponseDto shpCartData) {
		validateLinkCount(customerId);
		deactivatePaymentLink(customerId);
		deactivatePreviousLinkResend(customerId);	
		PaymentLinkRespDTO paymentLinkResp = new PaymentLinkRespDTO();
		
		String code = Random.randomAlphaNumeric(8);
		String hashVerifyCode = null;
		try {
			hashVerifyCode = com.amx.utils.CryptoUtil.getSHA2Hash(code);
			logger.info("Code Inside If : " + code);
			logger.info("HashCode Inside If : " + hashVerifyCode);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		if(shpCartData != null){
			List<CustomerShoppingCartDto> shoppingCartDetails = shpCartData.getShoppingCartDetails();
			
			if(shoppingCartDetails != null) {
				CustomerShoppingCartDto shpCartAppl = shoppingCartDetails.get(0);
				String remittanceApplications = shoppingCartDetails.stream()
						.map(i -> i.getRemittanceApplicationId().toString()).collect(Collectors.joining(","));
				
				PaygDetailsModel paymentApplication = new PaygDetailsModel();
				paymentApplication.setCustomerId(shpCartAppl.getCustomerId());
				paymentApplication.setPayAmount(shpCartData.getTotalNetAmount());
				paymentApplication.setApplIds(remittanceApplications);
				paymentApplication.setLinkActive(ConstantDocument.Yes);
				paymentApplication.setLinkDate(new Date());
				paymentApplication.setVerifycode(hashVerifyCode);
				paymentApplication.setPaymentType(ConstantDocument.DIRECT_LINK);
				paymentApplication.setCollDocFYear(shpCartAppl.getDocumentFinanceYear());
				
				fcSaleApplicationDao.savePaymentLinkApplication(paymentApplication);
				
				// Save link id against application Id's 
				PaygDetailsModel fetchPaymentLinkData = fcSaleApplicationDao.fetchPaymentLinkId(customerId, hashVerifyCode);
				if(fetchPaymentLinkData != null) {
					String applicationid = fetchPaymentLinkData.getApplIds();
					StringTokenizer tokenizer = new StringTokenizer(applicationid, ",");
			        
			        while (tokenizer.hasMoreTokens()) {
			        	String token = tokenizer.nextToken();
			            BigDecimal appId = new BigDecimal(token);
			            logger.info("Application Id : " + appId);
			            
			            //remittanceApplicationRepository.save(fetchApplication);
			            remittanceApplicationRepository.updateLinkId(appId, fetchPaymentLinkData.getPaygTrnxSeqId());
			        } 
				}
			}
		}
		
		PaygDetailsModel paymentLinkId = fcSaleApplicationDao.fetchPaymentLinkId(customerId, hashVerifyCode);
		if (paymentLinkId != null) {
			String currencyQuote = amxConfig.getDefCurrencyQuote();
			paymentLinkResp.setId(paymentLinkId.getPaygTrnxSeqId());
			paymentLinkResp.setVerificationCode(code);
			paymentLinkResp.setCurQutoe(currencyQuote);
			paymentLinkResp.setNetAmount(paymentLinkId.getPayAmount());
			//paymentLinkResp.setRequestData(paymentLinkId.getLinkDate());
			paymentLinkResp.setRequestData(new SimpleDateFormat("dd-MM-YYYY hh:mm").format(paymentLinkId.getLinkDate()));

			return paymentLinkResp;
		}
		
		
		return paymentLinkResp;
	}
	
	public PaymentLinkRespDTO validatePaymentLinkNew(BigDecimal linkId, String verificationCode) {
		PaymentLinkRespDTO paymentLinkResp = new PaymentLinkRespDTO();
		paymentLinkResp.setId(linkId);
		BigDecimal localCurrencyId = amxConfig.getDefaultCurrencyId();
		String currencyQuote = amxConfig.getDefCurrencyQuote();
		//BigDecimal localCurrencyId = metaData.getDefaultCurrencyId();
		String hashVerifyCode = null;
		try {
			hashVerifyCode = com.amx.utils.CryptoUtil.getSHA2Hash(verificationCode);
			logger.info("HashCode Inside If : " + hashVerifyCode);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		validatePreviiousDateLink(linkId);
		
		
		PaygDetailsModel paymentLink = fcSaleApplicationDao.validatePaymentLinkByCode(linkId, hashVerifyCode);
		if (paymentLink != null) {
			PaymentLinkRespStatus statusModel = new PaymentLinkRespStatus();
			if (paymentLink.getLinkActive().equals("Y")) {
				metaData.setCustomerId(paymentLink.getCustomerId());
				BranchRemittanceApplResponseDto shpCartData = branchRemittancePaymentManager
						.fetchCustomerShoppingCart(paymentLink.getCustomerId(), localCurrencyId);

				List<CustomerShoppingCartDto> shoppingCartDetails = shpCartData.getShoppingCartDetails();
				if (shoppingCartDetails != null) {
					BigDecimal totalNetAmounts = shpCartData.getTotalNetAmount();
					int value = totalNetAmounts.compareTo(paymentLink.getPayAmount());
					if (value != 0) {
						throw new GlobalException(JaxError.DIRECT_LINK_INVALID, "Link is invalid");
					}
					
					//Validate application Id's
					String applicationIds = paymentLink.getApplIds();
					validateApplicationIds(shoppingCartDetails, applicationIds);
					
				} else {
					throw new GlobalException(JaxError.DIRECT_LINK_INVALID, "Link Expired");
				}

				List<CustomerShoppingCartDto> custShopList = new ArrayList<>();

				String applicationid = paymentLink.getApplIds();
				StringTokenizer tokenizer = new StringTokenizer(applicationid, ",");
				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					BigDecimal appId = new BigDecimal(token);
					logger.info("Application Id : " + appId);
					if (shoppingCartDetails != null) {
						for (CustomerShoppingCartDto cartList : shoppingCartDetails) {
							if (cartList.getRemittanceApplicationId().equals(appId)) {
								custShopList.add(cartList);
							}
						}
					}
				}
				
				paymentLinkResp.setNetAmount(shpCartData.getTotalNetAmount());
				paymentLinkResp.setTotalTrnxAmount(shpCartData.getTotalLocalAmount());
				paymentLinkResp.setTotalCommissionAmt(shpCartData.getTotalTrnxFees());
				paymentLinkResp.setShoppingCartDetails(shoppingCartDetails);
				paymentLinkResp.setApplicationIds(paymentLink.getApplIds());
				paymentLinkResp.setCurQutoe(currencyQuote);
				paymentLinkResp.setLinkStatus(paymentLink.getLinkActive());
				paymentLinkResp.setMerchantTrackId(paymentLink.getCustomerId());
				paymentLinkResp.setDocumentFinancialYear(paymentLink.getCollDocFYear());
				paymentLinkResp.setDocumentIdForPayment(paymentLink.getPaygTrnxSeqId().toString());
				
				if(paymentLink.getResultCode() != null) {
					JaxTransactionStatus status = getJaxTransactionStatus(paymentLink);
					getPaymentLinkResponseStatus(statusModel, status, paymentLink);
					paymentLinkResp.setPaymentLinkRespStatus(statusModel);
				}
			}
			if(paymentLink.getLinkActive().equals("P")) {
				JaxTransactionStatus status = getJaxTransactionStatus(paymentLink);
				getPaymentLinkResponseStatus(statusModel, status, paymentLink);
								
				paymentLinkResp.setPaymentLinkRespStatus(statusModel);
				paymentLinkResp.setApplicationIds(paymentLink.getApplIds());
				paymentLinkResp.setLinkStatus(paymentLink.getLinkActive());
				paymentLinkResp.setCurQutoe(currencyQuote);
			}
			if (paymentLink.getLinkActive().equals("D")) {
				throw new GlobalException(JaxError.DIRECT_LINK_INVALID,
						"Payment link is deactivated");
			}
			

		} else {
			throw new GlobalException(JaxError.DIRECT_LINK_INVALID,
					"Invalidate link, Verification Code Mismatch");
		}
		return paymentLinkResp;
	}

	private void getPaymentLinkResponseStatus(PaymentLinkRespStatus statusModel, JaxTransactionStatus status,
			PaygDetailsModel paymentLink) {
		statusModel.setStatus(status);
		
		statusModel.setNetAmount(paymentLink.getPayAmount());
		statusModel.setTransactionReference(paymentLink.getApplIds());
		statusModel.setErrorMessage(paymentLink.getErrorMessage());
		
		ResponseCodeDetailDTO responseCodeDetail = new ResponseCodeDetailDTO();
		
		if(paymentLink.getResultCode() != null) {
			if(paymentLink.getResultCode().equals("NOT CAPTURED")) {
				paymentLink.setResultCode("NOT_CAPTURED");
			}
			responseCodeDetail = PayGRespCodeJSONConverter.getResponseCodeDetail(paymentLink.getResultCode());
		}
		responseCodeDetail.setPgPaymentId(paymentLink.getPgPaymentId());
		responseCodeDetail.setPgReferenceId(paymentLink.getPgReferenceId());
		responseCodeDetail.setPgTransId(paymentLink.getPgTransactionId());
		responseCodeDetail.setPgAuth(paymentLink.getPgAuthCode());
		
		statusModel.setResponseCodeDetail(responseCodeDetail);
		
	}

	private Boolean validatePreviiousDateLink(BigDecimal linkId) {
		try {
			List<PaygDetailsModel> listOfPaymentLink = fcSaleApplicationDao.validatePrevLink(linkId);
			if (!listOfPaymentLink.isEmpty() && listOfPaymentLink != null) {
				for (PaygDetailsModel payLink : listOfPaymentLink) {
					PaygDetailsModel paymentLink = pgRepository.findOne(payLink.getPaygTrnxSeqId());
					paymentLink.setLinkActive(ConstantDocument.Deleted);
					paymentLink.setModifiedDate(new Date());
					pgRepository.save(paymentLink);
					
					//throw new GlobalException(JaxError.DIRECT_LINK_DEACTIVATED,	"Payment link is deactivated");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("deActivate Payment Link failed for validate previous ");
		}
		return true;
	}

	public Boolean deactivatePaymentLink(BigDecimal customerId) {
		try {
			List<PaygDetailsModel> listOfPaymentLink = fcSaleApplicationDao.deactivatePreviousLink(customerId);
			if (!listOfPaymentLink.isEmpty() && listOfPaymentLink != null) {
				for (PaygDetailsModel payLink : listOfPaymentLink) {
					PaygDetailsModel paymentLink = pgRepository.findOne(payLink.getPaygTrnxSeqId());
					paymentLink.setLinkActive(ConstantDocument.Deleted);
					paymentLink.setModifiedDate(new Date());
					pgRepository.save(paymentLink);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("deActivate Payment Link failed for custoemr : " + customerId);
		}
		return true;
	}
	
	private Boolean deactivatePreviousLinkResend(BigDecimal customerId) {
		try {
			List<PaygDetailsModel> listOfPaymentLink = fcSaleApplicationDao.deactivatePreviousLinkResend(customerId);
			if (!listOfPaymentLink.isEmpty() && listOfPaymentLink != null) {
				for (PaygDetailsModel payLink : listOfPaymentLink) {
					PaygDetailsModel paymentLink = pgRepository.findOne(payLink.getPaygTrnxSeqId());
					paymentLink.setLinkActive(ConstantDocument.Deleted);
					paymentLink.setModifiedDate(new Date());
					pgRepository.save(paymentLink);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("deActivate Payment Link failed for custoemr : " + customerId);
		}
		return true;
	}

	private JaxTransactionStatus getJaxTransactionStatus(PaygDetailsModel paymentLink) {
		JaxTransactionStatus status = JaxTransactionStatus.APPLICATION_CREATED;
		String applicationStatus = paymentLink.getLinkActive();
		
		if (StringUtils.isBlank(applicationStatus) && paymentLink.getPgPaymentId() != null) {
			status = JaxTransactionStatus.PAYMENT_IN_PROCESS;
		}
		String resultCode = paymentLink.getResultCode();
		if ("CAPTURED".equalsIgnoreCase(resultCode)) {
			status = JaxTransactionStatus.PAYMENT_SUCCESS_APPLICATION_SUCCESS;

			/*if ("S".equals(applicationStatus) || "T".equals(applicationStatus)) {
				status = JaxTransactionStatus.PAYMENT_SUCCESS_APPLICATION_SUCCESS;
			} else {
				status = JaxTransactionStatus.PAYMENT_SUCCESS_APPLICATION_FAIL;
			}*/
		}
		if ("NOT CAPTURED".equalsIgnoreCase(resultCode)) {
			status = JaxTransactionStatus.PAYMENT_FAIL;
		}
		if ("CANCELED".equalsIgnoreCase(resultCode) || "CANCELLED".equalsIgnoreCase(resultCode)) {
			status = JaxTransactionStatus.PAYMENT_CANCELED_BY_USER;
		}

		return status;
	}

	public PaymentResponseDto paymentCaptureForPayLink(PaymentResponseDto paymentResponse, BigDecimal linkId) {
		logger.info("paymment capture :" + paymentResponse.toString());
		logger.info("Customer Id Direct Link: " + paymentResponse.getCustomerId());
		logger.info(
				"Result code :" + paymentResponse.getResultCode() + "\t Auth Code :" + paymentResponse.getAuth_appNo());
		logger.info("paymment capture Payment ID :" + paymentResponse.getPaymentId() + "\t Merchant Track Id :"
				+ paymentResponse.getTrackId() + "\t UDF 3 :" + paymentResponse.getUdf3() + "\t Udf 2 :"
				+ paymentResponse.getUdf2());
		
		metaData.setCustomerId(paymentResponse.getCustomerId());
		
		try {
			if (!StringUtils.isBlank(paymentResponse.getPaymentId())
					&& !StringUtils.isBlank(paymentResponse.getResultCode())
					&& (paymentResponse.getResultCode().equalsIgnoreCase(ConstantDocument.CAPTURED)
							|| paymentResponse.getResultCode().equalsIgnoreCase(ConstantDocument.APPROVED))) {
				// update payg details in payment link table
				fcSaleApplicationDao.updatePaygDetailsInPayLink(paymentResponse, linkId);
				
				//Send trnx successful notification
				if(paymentResponse.getCustomerId() != null) {
					PersonInfo personInfo = userService.getPersonInfo(paymentResponse.getCustomerId());
					if(personInfo!=null && !StringUtils.isBlank(personInfo.getEmail()) && personInfo.getEmail() != null) {
						notificationService.sendTransactionNotification(null, personInfo, null, false);
					}
				}
								
				//payment process to remittance
				PaygDetailsModel paymentLinkData =pgRepository.findOne(linkId);
				BranchRemittanceRequestModel request = new BranchRemittanceRequestModel();
				List<BranchApplicationDto> remittanceApplicationIds =new ArrayList<>();
				List<RemittanceCollectionDto> collctionModeDto = new ArrayList<>();
				//List<UserStockDto> currencyRefundDenomination = new ArrayList<>();
				
				PaymentModeModel payModeModel = payModeRepositoy.getPaymentModeDetails(ConstantDocument.KNET_CODE);
				
				RemittanceCollectionDto remittanceCollection = new RemittanceCollectionDto();
				//remittanceCollection.setPaymentModeId(paymentLinkData.getPaygTrnxSeqId());
				remittanceCollection.setPaymentModeId(payModeModel.getPaymentModeId());
				remittanceCollection.setPaymentAmount(paymentLinkData.getPayAmount());
				remittanceCollection.setApprovalNo(paymentLinkData.getPgAuthCode());
				collctionModeDto.add(remittanceCollection);
				
				BigDecimal totalLoyaltyEncashed =BigDecimal.ZERO;
				BigDecimal totalPaidAmount =BigDecimal.ZERO;
				
				logger.info("Ex APPL TRNX Query Start ------> ");
				String[] appIds = paymentLinkData.getApplIds().split(",");
				List<BigDecimal> appIdsBigDecimalList = Arrays.asList(appIds).stream().map(i-> new BigDecimal(i)).collect(Collectors.toList());
				List<RemittanceApplication> applications = remittanceApplicationRepository.getApplicationList(appIdsBigDecimalList);
				//List<RemittanceApplication> applications = remittanceApplicationRepository.getApplByPaymentlinkId(linkId);
				logger.info("Ex APPL TRNX Query End ------> ");
				
				if(null != applications){
					for(RemittanceApplication appl: applications) {
						BranchApplicationDto applDto = new BranchApplicationDto();
				        applDto.setApplicationId(appl.getRemittanceApplicationId());
						totalLoyaltyEncashed=totalLoyaltyEncashed.add(appl.getLoyaltyPointsEncashed());
						totalPaidAmount=totalPaidAmount.add(appl.getLocalNetTranxAmount());
						remittanceApplicationIds.add(applDto);
					}
				}				
				
				//Set request Parameter
				logger.info("request Parameter SET for saveRemittanceTrnx ------> ");
				request.setRemittanceApplicationId(remittanceApplicationIds);
				request.setCollctionModeDto(collctionModeDto);
				request.setCurrencyRefundDenomination(null);
				request.setTotalTrnxAmount(paymentLinkData.getPayAmount());
				request.setTotalLoyaltyAmount(totalLoyaltyEncashed);
				request.setPaidAmount(totalPaidAmount);
				
				branchRemittanceSaveManager.saveRemittanceTrnx(request);
				
			} else {
				fcSaleApplicationDao.updatePaygDetailsFail(paymentResponse, linkId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return paymentResponse;
	}

	public void validateHomesendApplication(List<CustomerShoppingCartDto> shoppingCartDetails) {
		for(CustomerShoppingCartDto shpCartData : shoppingCartDetails) {
			if(shpCartData.getBankIndicator() != null && shpCartData.getBankIndicator().equals("SB")) {
				throw new GlobalException(JaxError.HOMESEND_APPL_NOT_ALLOWED, "Home Send Application not allowed");
			}
		}
		
	}
	
	private void validateApplicationIds(List<CustomerShoppingCartDto> shoppingCartDetails, String applicationIds) {
		List<String> list = Arrays.asList(applicationIds.split(","));
		List<BigDecimal> l = new ArrayList<BigDecimal>();
		for (String value : list) {
		    l.add(new BigDecimal(value));
		}
		
		List<BigDecimal> l2 = new ArrayList<BigDecimal>();
		for(CustomerShoppingCartDto shpCartData : shoppingCartDetails) {
			l2.add(shpCartData.getApplicationDetailsId());
		}
		
		 Collections.sort(l);
	     Collections.sort(l2);
	     
	     if(!l.equals(l2)) {
	    	 throw new GlobalException(JaxError.DIRECT_LINK_INVALID,
						"Invalidate link, Application Id mismatch");
	     }
	     
	     
	}
	
	private void validateLinkCount(BigDecimal customerId) {
		CustomerCartMaster linkCountData = customerCartDao.getCartData(customerId);
		
		final Integer MAX_LINK_COUNT = jaxTenantProperties.getDirectLinkCount();
		if(linkCountData != null) {
			if(linkCountData.getLinkCount() != null) {
				int lockCnt = linkCountData.getLinkCount().intValue();
				if (lockCnt == MAX_LINK_COUNT.intValue()) {
					throw new GlobalException(JaxError.LINK_COUNT_ATTEMPT_EXCEEDED, "Payment link send limit exceeded");
				}
			}
		}
	}

	public void increaseLinkCount(BigDecimal customerId) {
		CustomerCartMaster cartData = customerCartDao.getCartData(customerId);
		Integer lockCnt = 0;
		if(cartData != null){
			if(cartData.getLinkCount() != null) {
				lockCnt = cartData.getLinkCount().intValue();
			}
			lockCnt++;
			cartData.setLinkCount(new BigDecimal(lockCnt));
			
			customerCartRepository.save(cartData);
		}
	}

}
