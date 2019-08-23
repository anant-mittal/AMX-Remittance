package com.amx.jax.branchremittance.manager;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
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
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.BranchRemittancePaymentDao;
import com.amx.jax.dao.DirectPaymentLinkDao;
import com.amx.jax.dbmodel.remittance.PaymentLinkModel;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dbmodel.remittance.ShoppingCartDetails;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.response.remittance.PaymentLinkRespDTO;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.repository.IPaymentLinkDetailsRepository;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.utils.Random;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class DirectPaymentLinkManager extends AbstractModel {

	Logger logger = LoggerFactory.getLogger(getClass());
	private static final long serialVersionUID = 1L;

	@Autowired
	DirectPaymentLinkDao directPaymentLinkDao;

	@Autowired
	IPaymentLinkDetailsRepository paymentLinkDetailsRepository;

	@Autowired
	BranchRemittancePaymentDao branchRemittancePaymentDao;
	
	@Autowired
	RemittanceApplicationRepository remittanceApplicationRepository;

	public PaymentLinkRespDTO fetchPaymentLinkDetails(BigDecimal customerId, BigDecimal localCurrencyId) {

		deactivatePaymentLink(customerId);

		PaymentLinkRespDTO paymentLinkResp = new PaymentLinkRespDTO();
		List<ShoppingCartDetails> lstCustomerShopping = branchRemittancePaymentDao
				.fetchCustomerShoppingCart(customerId);
		List<PaymentLinkModel> paymentLinkData = new ArrayList<>();
		String code = Random.randomAlphaNumeric(4);
		String hashVerifyCode = null;
		try {
			hashVerifyCode = com.amx.utils.CryptoUtil.getSHA2Hash(code);
			logger.info("Code Inside If : " + code);
			logger.info("HashCode Inside If : " + hashVerifyCode);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		if (lstCustomerShopping != null && !lstCustomerShopping.isEmpty() && lstCustomerShopping.size() != 0) {
			ShoppingCartDetails shpCartAppl = lstCustomerShopping.get(0);

			double totalNetAmountDouble = lstCustomerShopping.stream()
					.mapToDouble(i -> i.getLocalNextTranxAmount().doubleValue()).sum();
			BigDecimal totalNetAmounts = new BigDecimal(totalNetAmountDouble);
			String remittanceApplications = lstCustomerShopping.stream()
					.map(i -> i.getRemittanceApplicationId().toString()).collect(Collectors.joining(","));

			PaymentLinkModel paymentApplication = new PaymentLinkModel();
			paymentApplication.setApplicationCountryId(new BigDecimal(91));
			paymentApplication.setCustomerId(shpCartAppl.getCustomerId());
			paymentApplication.setPaymentAmount(totalNetAmounts);
			paymentApplication.setApplicationIds(remittanceApplications);
			paymentApplication.setIsActive("Y");
			paymentApplication.setLinkDate(new Date());
			paymentApplication.setVerificationCode(hashVerifyCode);

			paymentLinkData.add(paymentApplication);
			directPaymentLinkDao.savePaymentLinkDetails(paymentLinkData);
			
			// Save link id against application Id's 
			PaymentLinkModel fetchPaymentLinkData = directPaymentLinkDao.fetchPaymentLinkId(customerId, hashVerifyCode);
			if(fetchPaymentLinkData != null) {
				String applicationid = fetchPaymentLinkData.getApplicationIds();
				StringTokenizer tokenizer = new StringTokenizer(applicationid, ",");
		        
		        while (tokenizer.hasMoreTokens()) {
		        	String token = tokenizer.nextToken();
		            BigDecimal appId = new BigDecimal(token);
		            logger.info("Application Id : " + appId);
		            RemittanceApplication fetchApplication = remittanceApplicationRepository.fetchByRemittanceApplicationId(appId);
		            fetchApplication.setPaymentLinkId(fetchPaymentLinkData.getLinkId());
		            
		            remittanceApplicationRepository.save(fetchApplication);
		        } 
			}
		}

		logger.info("HashCode Outside If : " + hashVerifyCode);
		PaymentLinkModel paymentLinkId = directPaymentLinkDao.fetchPaymentLinkId(customerId, hashVerifyCode);
		if (paymentLinkId != null) {
			paymentLinkResp.setId(paymentLinkId.getLinkId());
			paymentLinkResp.setVerificationCode(code);
			paymentLinkResp.setCurQutoe("KD");
			paymentLinkResp.setAmount(paymentLinkId.getPaymentAmount());
			paymentLinkResp.setRequestData(paymentLinkId.getLinkDate());

			return paymentLinkResp;
		}

		return paymentLinkResp;
	}

	public Boolean deactivatePaymentLink(BigDecimal customerId) {
		try {
			List<PaymentLinkModel> listOfPaymentLink = directPaymentLinkDao.deactivatePreviousLink(customerId);
			if (!listOfPaymentLink.isEmpty() && listOfPaymentLink != null) {
				for (PaymentLinkModel payLink : listOfPaymentLink) {
					PaymentLinkModel paymentLink = paymentLinkDetailsRepository.findOne(payLink.getLinkId());
					paymentLink.setIsActive("D");
					paymentLink.setModifiedDate(new Date());
					paymentLinkDetailsRepository.save(paymentLink);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("deActivate Payment Link failed for custoemr : " + customerId);
		}
		return true;
	}

	public PaymentLinkRespDTO validatePayLink(BigDecimal linkId, String verificationCode) {
		PaymentLinkRespDTO paymentLinkResp = new PaymentLinkRespDTO();
		String hashVerifyCode = null;
		try {
			hashVerifyCode = com.amx.utils.CryptoUtil.getSHA2Hash(verificationCode);
			logger.info("HashCode Inside If : " + hashVerifyCode);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		PaymentLinkModel paymentLink = directPaymentLinkDao.validatePaymentLinkByCode(linkId, hashVerifyCode);
		if (paymentLink != null) {
			paymentLinkResp.setApplicationIds(paymentLink.getApplicationIds());
		} else {
			throw new GlobalException(JaxError.VERIFICATION_CODE_MISMATCH,
					"Invalidate link, Verification Code Mismatch");
		}
		return paymentLinkResp;
	}

	public PaymentResponseDto paymentCaptureForPayLink(PaymentResponseDto paymentResponse, BigDecimal linkId) {
		logger.info("paymment capture :" + paymentResponse.toString());
		logger.info("Customer Id :" + paymentResponse.getCustomerId());
		logger.info(
				"Result code :" + paymentResponse.getResultCode() + "\t Auth Code :" + paymentResponse.getAuth_appNo());
		logger.info("paymment capture Payment ID :" + paymentResponse.getPaymentId() + "\t Merchant Track Id :"
				+ paymentResponse.getTrackId() + "\t UDF 3 :" + paymentResponse.getUdf3() + "\t Udf 2 :"
				+ paymentResponse.getUdf2());
		
		try {
			if (!StringUtils.isBlank(paymentResponse.getPaymentId())
					&& !StringUtils.isBlank(paymentResponse.getResultCode())
					&& (paymentResponse.getResultCode().equalsIgnoreCase(ConstantDocument.CAPTURED)
							|| paymentResponse.getResultCode().equalsIgnoreCase(ConstantDocument.APPROVED))) {
				// update payg details in payment link table
				directPaymentLinkDao.updatePaygDetailsInPayLink(paymentResponse, linkId);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}

	

}
