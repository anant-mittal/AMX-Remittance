package com.amx.jax.branchremittance.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.branchremittance.manager.DirectPaymentLinkManager;
import com.amx.jax.model.response.remittance.PaymentLinkRespDTO;
import com.amx.jax.services.AbstractService;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DirectPaymentLinkService extends AbstractService {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	DirectPaymentLinkManager directPaymentLinkManager;

	public PaymentLinkRespDTO fetchPaymentLinkDetails(BigDecimal customerId, BigDecimal localCurrencyId) {
		PaymentLinkRespDTO paymentdto = directPaymentLinkManager.fetchPaymentLinkDetails(customerId, localCurrencyId);

		return paymentdto;
	}

	public PaymentLinkRespDTO validatePayLink(BigDecimal linkId, String verificationCode) {
		PaymentLinkRespDTO paymentdto = directPaymentLinkManager.validatePayLink(linkId, verificationCode);
		return paymentdto;
	}

}
