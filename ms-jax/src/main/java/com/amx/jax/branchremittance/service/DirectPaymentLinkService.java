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
import com.amx.jax.branchremittance.manager.BranchRemittancePaymentManager;
import com.amx.jax.branchremittance.manager.DirectPaymentLinkManager;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.model.response.customer.CustomerDto;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.jax.model.response.remittance.BranchRemittanceApplResponseDto;
import com.amx.jax.model.response.remittance.PaymentLinkRespDTO;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.services.AbstractService;
import com.amx.jax.userservice.service.UserService;
import com.amx.utils.EntityDtoUtil;
import com.amx.utils.JsonUtil;

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
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	BranchRemittancePaymentManager branchRemittancePaymentManager;

	public PaymentLinkRespDTO fetchPaymentLinkDetails(BigDecimal customerId, BigDecimal localCurrencyId) {
		
		BranchRemittanceApplResponseDto shpCartData = branchRemittancePaymentManager.fetchCustomerShoppingCart(customerId, localCurrencyId);
		//PaymentLinkRespDTO paymentdto = directPaymentLinkManager.fetchPaymentLinkDetails(customerId, localCurrencyId);
		
		PaymentLinkRespDTO paymentDtoNEW = directPaymentLinkManager.getPaymentLinkDetails(customerId, shpCartData);
		
		PersonInfo personInfo = userService.getPersonInfo(customerId);
		sendDirectLinkEmail(paymentDtoNEW, personInfo, customerId);
		sendDirectLinkSMS(paymentDtoNEW, personInfo, customerId);

		return paymentDtoNEW;
	}

	public PaymentLinkRespDTO validatePayLink(BigDecimal linkId, String verificationCode) {
		//PaymentLinkRespDTO paymentdto = directPaymentLinkManager.validatePayLink(linkId, verificationCode);
		PaymentLinkRespDTO paymentdtoNew = directPaymentLinkManager.validatePaymentLinkNew(linkId, verificationCode);
		return paymentdtoNew;
	}

	public void sendDirectLinkEmail(PaymentLinkRespDTO paymentdto, PersonInfo personInfo,BigDecimal customerId) {

		Customer customer = customerRepository.getActiveCustomerDetailsByCustomerId(customerId);
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
				directLinkEmail.getModel().put("link", paymentdto);
				directLinkEmail.getModel().put("customer", EntityDtoUtil.entityToDto(customer, new CustomerDto()));
				directLinkEmail.getModel().put(NotificationConstants.RESP_DATA_KEY, paymentdto);
				logger.info("Payment Link ENTIRE JSON : "+JsonUtil.toJson(directLinkEmail.getModel()));
				postManService.sendEmailAsync(directLinkEmail);
			}
			
		} catch (Exception e) {
			logger.error("Error while sending MAIL for Direct Link : " + e.getMessage());
		}
	
		
	}
	
	private void sendDirectLinkSMS(PaymentLinkRespDTO paymentdto, PersonInfo personInfo, BigDecimal customerId) {
		Customer customer = customerRepository.getActiveCustomerDetailsByCustomerId(customerId);
		if(paymentdto != null) {
			logger.info(String.format("Sending mOTP SMS to customer :%s on mobile_no :%s  ", personInfo.getFirstName(),
					personInfo.getMobile()));

			SMS sms = new SMS();
			sms.addTo(personInfo.getMobile());
			sms.getModel().put("link", paymentdto);
			sms.getModel().put("customer", EntityDtoUtil.entityToDto(customer, new CustomerDto()));
			sms.getModel().put(NotificationConstants.RESP_DATA_KEY, paymentdto);
			sms.setITemplate(TemplatesMX.PAYMENT_LINK);

			try {
				postManService.sendSMSAsync(sms);
			} catch (PostManException e) {
				logger.error("Error while sending SMS for Direct Link : ", e.getMessage());
			}
		}
	}

	public AmxApiResponse<PaymentResponseDto, Object> saveDirectLinkPayment(PaymentResponseDto paymentResponse, BigDecimal linkId) {
		PaymentResponseDto paymentResponseDto =directPaymentLinkManager.paymentCaptureForPayLink(paymentResponse, linkId);
		return AmxApiResponse.build(paymentResponseDto);
	}
}
