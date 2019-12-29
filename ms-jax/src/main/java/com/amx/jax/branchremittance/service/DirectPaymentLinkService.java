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
import com.amx.jax.dict.AmxEnums.CommunicationEvents;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.customer.CustomerDto;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.jax.model.response.remittance.BranchRemittanceApplResponseDto;
import com.amx.jax.model.response.remittance.PaymentLinkRespDTO;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.client.WhatsAppClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.services.AbstractService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.CommunicationPrefsUtil;
import com.amx.jax.util.CommunicationPrefsUtil.CommunicationPrefsResult;
import com.amx.jax.validation.FxOrderValidation;
import com.amx.utils.EntityDtoUtil;
import com.amx.utils.JsonUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DirectPaymentLinkService extends AbstractService {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	FxOrderValidation validation;
	
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
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	CommunicationPrefsUtil communicationPrefsUtil;
	
	@Autowired
	CustomerDao custDao;
	
	@Autowired
	WhatsAppClient whatsAppClient;
	
	@Autowired
	PushNotifyClient pushNotifyClient;

	public AmxApiResponse<PaymentLinkRespDTO, Object> fetchPaymentLinkDetails() {
		
		validation.validateHeaderInfo();
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal localCurrencyId = metaData.getDefaultCurrencyId();
		
		BranchRemittanceApplResponseDto shpCartData = branchRemittancePaymentManager.fetchCustomerShoppingCart(customerId, localCurrencyId);
		PaymentLinkRespDTO paymentDtoNEW = directPaymentLinkManager.getPaymentLinkDetails(customerId, shpCartData);
		if(shpCartData.getShoppingCartDetails() != null) {
			directPaymentLinkManager.validateHomesendApplication(shpCartData.getShoppingCartDetails());
		}
		
		PersonInfo personInfo = userService.getPersonInfo(customerId);
		Customer customer = custDao.getActiveCustomerDetailsByCustomerId(customerId);
		CommunicationPrefsResult communicationPrefsResult = communicationPrefsUtil.forCustomer(CommunicationEvents.PAYMENT_LINK, customer);
		if(communicationPrefsResult.isEmail()) {
			sendDirectLinkEmail(paymentDtoNEW, personInfo, customerId);
		}
		if(communicationPrefsResult.isSms()) {
			sendDirectLinkSMS(paymentDtoNEW, personInfo, customerId);
		}
		if(communicationPrefsResult.isWhatsApp()) {
			sendDirectLinkWhatsApp(paymentDtoNEW, personInfo, customerId);
		}
		if(communicationPrefsResult.isPushNotify()) {
			sendDirectLinkPushNotification(paymentDtoNEW, personInfo, customerId);
		}

		return AmxApiResponse.build(paymentDtoNEW);
	}

	//@Transactional
	public AmxApiResponse<PaymentLinkRespDTO, Object> validatePayLink(BigDecimal linkId, String verificationCode) {
		PaymentLinkRespDTO paymentdtoNew = directPaymentLinkManager.validatePaymentLinkNew(linkId, verificationCode);
		return AmxApiResponse.build(paymentdtoNew);
	}

	public void sendDirectLinkEmail(PaymentLinkRespDTO paymentdto, PersonInfo personInfo,BigDecimal customerId) {

		Customer customer = customerRepository.getActiveCustomerDetailsByCustomerId(customerId);
		try {
			if (paymentdto != null) {
				logger.info("Sending Direct Link Email to customer : ");
				Email directLinkEmail = new Email();
				directLinkEmail.setSubject("Payment Link for your remittance request");
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
	
	private void sendDirectLinkWhatsApp(PaymentLinkRespDTO paymentdto, PersonInfo personInfo, BigDecimal customerId) {
		Customer customer = customerRepository.getActiveCustomerDetailsByCustomerId(customerId);
		if(paymentdto != null) {
			logger.info(String.format("Sending mOTP SMS to customer :%s on mobile_no :%s  ", personInfo.getFirstName(),
					personInfo.getMobile()));

			WAMessage waMessage = new WAMessage();
			waMessage.addTo(personInfo.getMobile());
			waMessage.getModel().put("link", paymentdto);
			waMessage.getModel().put("customer", EntityDtoUtil.entityToDto(customer, new CustomerDto()));
			waMessage.getModel().put(NotificationConstants.RESP_DATA_KEY, paymentdto);
			waMessage.setITemplate(TemplatesMX.PAYMENT_LINK);

			try {
				whatsAppClient.send(waMessage);
			} catch (PostManException e) {
				logger.error("Error while sending SMS for Direct Link : ", e.getMessage());
			}
		}
	}
	
	private void sendDirectLinkPushNotification(PaymentLinkRespDTO paymentdto, PersonInfo personInfo, BigDecimal customerId) {
		Customer customer = customerRepository.getActiveCustomerDetailsByCustomerId(customerId);
		if(paymentdto != null) {
			logger.info(String.format("Sending mOTP SMS to customer :%s on mobile_no :%s  ", personInfo.getFirstName(),
					personInfo.getMobile()));

			PushMessage pushMessage = new PushMessage();
			pushMessage.addToUser(customerId);
			pushMessage.getModel().put("link", paymentdto);
			pushMessage.getModel().put("customer", EntityDtoUtil.entityToDto(customer, new CustomerDto()));
			pushMessage.getModel().put(NotificationConstants.RESP_DATA_KEY, paymentdto);
			pushMessage.setITemplate(TemplatesMX.PAYMENT_LINK);

			try {
				pushNotifyClient.send(pushMessage);
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
