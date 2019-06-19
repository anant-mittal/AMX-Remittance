package com.amx.jax.customer.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.CustomerVerificationType;
import com.amx.jax.customer.manager.CustomerContactVerificationManager;
import com.amx.jax.db.utils.EntityDtoUtil;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerContactVerification;
import com.amx.jax.dbmodel.CustomerVerification;
import com.amx.jax.dict.ContactType;
import com.amx.jax.dict.Language;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.customer.CustomerDto;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.services.AbstractService;
import com.amx.jax.userservice.service.CustomerVerificationService;
import com.amx.jax.util.AmxDBConstants.Status;
import com.amx.utils.JsonUtil;

@Service
public class JaxCustomerContactVerificationService extends AbstractService {
	
	
	Logger logger = Logger.getLogger(JaxCustomerContactVerificationService.class);
	@Autowired
	CustomerContactVerificationManager customerContactVerificationManager;
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	MetaData metaData;
	@Autowired
	PostManService postManService;
	@Autowired
	private CustomerVerificationService customerVerificationService;
	
	public void sendEmailVerifyLinkOnReg(BigDecimal customerId) {
		Customer customer = customerRepository.getCustomerByCustomerIdAndIsActive(customerId, "Y");
		CustomerContactVerification customerContactVerification = customerContactVerificationManager.create(customer, ContactType.EMAIL);
		
		
		Email email = new Email();
		
		
		logger.debug("contact verification link object is "+customerContactVerification.toString());
		
		email.setITemplate(TemplatesMX.CONTACT_VERIFICATION_EMAIL);
		email.getModel().put("customer", EntityDtoUtil.entityToDto(customer, new CustomerDto()));
		email.getModel().put("link", customerContactVerification);
		email.addTo(customer.getEmail());
		email.setHtml(true);
		sendEmail(email);
	}
	
	@Async(ExecutorConfig.DEFAULT)
	public void sendEmail(Email email) {
		try {
			logger.debug("email sent");
			postManService.sendEmailAsync(email);
		} catch (PostManException e) {
			logger.debug("customer email verifivcation exception");
			
		}
	}
	
	public void validateEmailVerification(BigDecimal customerId) {
		Customer customer = customerRepository.getCustomerByCustomerIdAndIsActive(customerId, "Y");
		if(customer.getEmailVerified()==Status.Y) {
			CustomerVerification cv = customerVerificationService.getVerification(metaData.getCustomerId(),
					CustomerVerificationType.EMAIL);
			cv.setVerificationStatus(ConstantDocument.Yes);
			
		}
	}
		
	} 
	

