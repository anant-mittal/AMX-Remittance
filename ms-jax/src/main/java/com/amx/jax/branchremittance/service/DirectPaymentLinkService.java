package com.amx.jax.branchremittance.service;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.NotificationConstants;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branchremittance.manager.DirectPaymentLinkManager;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.jax.model.response.remittance.PaymentLinkRespDTO;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.services.AbstractService;
import com.amx.jax.userservice.service.UserService;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DirectPaymentLinkService extends AbstractService {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	DirectPaymentLinkManager directPaymentLinkManager;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PostManService postManService;

	public PaymentLinkRespDTO fetchPaymentLinkDetails(BigDecimal customerId, BigDecimal localCurrencyId) {
		PaymentLinkRespDTO paymentdto = directPaymentLinkManager.fetchPaymentLinkDetails(customerId, localCurrencyId);
		
		PersonInfo personInfo = userService.getPersonInfo(customerId);
		sendDirectLinkEmail(paymentdto, personInfo);
		sendDirectLinkSMS(paymentdto, personInfo);

		return paymentdto;
	}

	public PaymentLinkRespDTO validatePayLink(BigDecimal linkId, String verificationCode) {
		PaymentLinkRespDTO paymentdto = directPaymentLinkManager.validatePayLink(linkId, verificationCode);
		return paymentdto;
	}

	public void sendDirectLinkEmail(PaymentLinkRespDTO paymentdto, PersonInfo personInfo) {

		try {
			if (paymentdto != null) {
				logger.info("Sending Direct Link Email to customer : ");
				Email directLinkEmail = new Email();
				directLinkEmail.setSubject("Direct Link from Al Mulla Exchange.");
				if (personInfo.getEmail() != null && !StringUtils.isBlank(personInfo.getEmail())) {
					directLinkEmail.addTo(personInfo.getEmail());
				}
				directLinkEmail.setITemplate(TemplatesMX.PAYMENT_LINK);
				directLinkEmail.setHtml(true);
				directLinkEmail.getModel().put(NotificationConstants.RESP_DATA_KEY, paymentdto);
				postManService.sendEmailAsync(directLinkEmail);
			}
			
		} catch (Exception e) {
			logger.error("Error while sending mail WantIT BuyIT : " + e.getMessage());
		}
	
		
	}
	
	private void sendDirectLinkSMS(PaymentLinkRespDTO paymentdto, PersonInfo personInfo) {
		if(paymentdto != null) {
			logger.info(String.format("Sending mOTP SMS to customer :%s on mobile_no :%s  ", personInfo.getFirstName(),
					personInfo.getMobile()));

			SMS sms = new SMS();
			sms.addTo(personInfo.getMobile());
			sms.getModel().put(NotificationConstants.RESP_DATA_KEY, paymentdto);
			sms.setITemplate(TemplatesMX.PAYMENT_LINK);

			try {
				postManService.sendSMSAsync(sms);
			} catch (PostManException e) {
				logger.error("error in sendLinkSms", e);
			}
		}
	}

	public AmxApiResponse<PaymentResponseDto, Object> saveDirectLinkPayment(PaymentResponseDto paymentResponse, BigDecimal linkId) {
		PaymentResponseDto paymentResponseDto =directPaymentLinkManager.paymentCaptureForPayLink(paymentResponse, linkId);
		return AmxApiResponse.build(paymentResponseDto);
	}
}
