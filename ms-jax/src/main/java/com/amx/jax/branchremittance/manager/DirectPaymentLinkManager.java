package com.amx.jax.branchremittance.manager;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.JaxTransactionStatus;
import com.amx.jax.dao.BranchRemittancePaymentDao;
import com.amx.jax.dao.FcSaleApplicationDao;
import com.amx.jax.dbmodel.PaygDetailsModel;
import com.amx.jax.dbmodel.PaymentModeModel;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dict.PayGRespCodeJSONConverter;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.request.remittance.BranchApplicationDto;
import com.amx.jax.model.request.remittance.BranchRemittanceRequestModel;
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
	
	public PaymentLinkRespDTO getPaymentLinkDetails(BigDecimal customerId, BranchRemittanceApplResponseDto shpCartData) {
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
			            RemittanceApplication fetchApplication = remittanceApplicationRepository.fetchByRemittanceApplicationId(appId);
			            fetchApplication.setPaymentLinkId(fetchPaymentLinkData.getPaygTrnxSeqId());
			            
			            remittanceApplicationRepository.save(fetchApplication);
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
					int value = paymentLink.getPayAmount().compareTo(totalNetAmounts);
					if (value == -1) {
						throw new GlobalException(JaxError.AMOUNT_MISMATCH, "Amount is Mismatch, Invalid Link");
					}
				} else {
					throw new GlobalException(JaxError.DIRECT_LINK_EXPIRED, "Link Expired");
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
			}
			if(paymentLink.getLinkActive().equals("P")) {
				JaxTransactionStatus status = getJaxTransactionStatus(paymentLink);
				statusModel.setStatus(status);
				
				statusModel.setNetAmount(paymentLink.getPayAmount());
				statusModel.setTransactionReference(paymentLink.getApplIds());
				statusModel.setErrorMessage(paymentLink.getErrorMessage());
				
				ResponseCodeDetailDTO responseCodeDetail = new ResponseCodeDetailDTO();
				
				if(paymentLink.getResultCode() != null) {
					responseCodeDetail = PayGRespCodeJSONConverter.getResponseCodeDetail(paymentLink.getResultCode());
				}
				responseCodeDetail.setPgPaymentId(paymentLink.getPgPaymentId());
				responseCodeDetail.setPgReferenceId(paymentLink.getPgReferenceId());
				responseCodeDetail.setPgTransId(paymentLink.getPgTransactionId());
				responseCodeDetail.setPgAuth(paymentLink.getPgAuthCode());
				
				statusModel.setResponseCodeDetail(responseCodeDetail);
				
				paymentLinkResp.setPaymentLinkRespStatus(statusModel);
				paymentLinkResp.setApplicationIds(paymentLink.getApplIds());
				paymentLinkResp.setLinkStatus(paymentLink.getLinkActive());
			}
			if (paymentLink.getLinkActive().equals("D")) {
				throw new GlobalException(JaxError.DIRECT_LINK_DEACTIVATED,
						"Payment link is deactivated");
			}
			

		} else {
			throw new GlobalException(JaxError.VERIFICATION_CODE_MISMATCH,
					"Invalidate link, Verification Code Mismatch");
		}
		return paymentLinkResp;
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
				
				//payment process to remittance
				PaygDetailsModel paymentLinkData =pgRepository.findOne(linkId);
				BranchRemittanceRequestModel request = new BranchRemittanceRequestModel();
				List<BranchApplicationDto> remittanceApplicationIds =new ArrayList<>();
				List<RemittanceCollectionDto> collctionModeDto = new ArrayList<>();
				//List<UserStockDto> currencyRefundDenomination = new ArrayList<>();
				BranchApplicationDto remitApplicationId = new BranchApplicationDto();
				
				String applicationid = paymentLinkData.getApplIds();
				StringTokenizer tokenizer = new StringTokenizer(applicationid, ",");
				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					BigDecimal appId = new BigDecimal(token);
					logger.info("Application Id : " + appId);
					remitApplicationId.setApplicationId(appId);
					remittanceApplicationIds.add(remitApplicationId);
				}
				
				PaymentModeModel payModeModel = payModeRepositoy.getPaymentModeDetails(ConstantDocument.KNET_CODE);
				
				RemittanceCollectionDto remittanceCollection = new RemittanceCollectionDto();
				//remittanceCollection.setPaymentModeId(paymentLinkData.getPaygTrnxSeqId());
				remittanceCollection.setPaymentModeId(payModeModel.getPaymentModeId());
				remittanceCollection.setPaymentAmount(paymentLinkData.getPayAmount());
				remittanceCollection.setApprovalNo(paymentLinkData.getPgAuthCode());
				
				logger.info("Payment value set in remittanceCollection : "+paymentLinkData.getPayAmount());
				
				collctionModeDto.add(remittanceCollection);
				
				BigDecimal totalLoyaltyEncashed =BigDecimal.ZERO;
				BigDecimal totalPaidAmount =BigDecimal.ZERO;
				List<RemittanceApplication> applications = remittanceApplicationRepository.getApplByPaymentlinkId(linkId);
				
				if(null != applications){
					logger.info("applications count ------> : " +applications);
					for(RemittanceApplication appl: applications) {
						totalLoyaltyEncashed=totalLoyaltyEncashed.add(appl.getLoyaltyPointsEncashed());
						totalPaidAmount=totalPaidAmount.add(appl.getLocalTranxAmount());
					}
				}	
				
				logger.info("Total Paid Amt ------> : " +totalPaidAmount);
				
				
				//Set request Parameter
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

	

	

}
