package com.amx.jax.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.remittance.PaymentLinkModel;
import com.amx.jax.repository.IPaymentLinkDetailsRepository;

@Component
public class DirectPaymentLinkDao {

	@Autowired
	IPaymentLinkDetailsRepository paymentLinkDetailsRepository;

	public void savePaymentLinkDetails(List<PaymentLinkModel> paymentLinkData) {
		paymentLinkDetailsRepository.save(paymentLinkData);
	}

	public List<PaymentLinkModel> deactivatePreviousLink(BigDecimal customerId) {
		return paymentLinkDetailsRepository.deactivatePrevLink(customerId);
	}

	public PaymentLinkModel fetchPaymentLinkId(BigDecimal customerId, String hashVerifyCode) {
		return paymentLinkDetailsRepository.fetchPayLinkIdForCustomer(customerId, hashVerifyCode);
	}

	public PaymentLinkModel validatePaymentLinkByCode(BigDecimal linkId, String verificationCode) {
		return paymentLinkDetailsRepository.fetchPaymentByLinkIdandCode(linkId, verificationCode);
	}
}
