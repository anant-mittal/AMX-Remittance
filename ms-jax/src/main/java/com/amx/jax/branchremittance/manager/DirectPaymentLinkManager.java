package com.amx.jax.branchremittance.manager;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.dao.BranchRemittancePaymentDao;
import com.amx.jax.dao.DirectPaymentLinkDao;
import com.amx.jax.dbmodel.remittance.PaymentLinkModel;
import com.amx.jax.dbmodel.remittance.ShoppingCartDetails;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.response.remittance.PaymentLinkRespDTO;
import com.amx.jax.repository.IPaymentLinkDetailsRepository;
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
		}

		logger.info("HashCode Outside If : " + hashVerifyCode);
		PaymentLinkModel paymentLinkId = directPaymentLinkDao.fetchPaymentLinkId(customerId, hashVerifyCode);
		if (paymentLinkId != null) {
			paymentLinkResp.setLinkId(paymentLinkId.getLinkId());
			paymentLinkResp.setVerificationCode(code);

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

}
