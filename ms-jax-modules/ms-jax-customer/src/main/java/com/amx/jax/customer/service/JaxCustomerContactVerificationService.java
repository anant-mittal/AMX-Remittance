package com.amx.jax.customer.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.exception.jax.UserNotFoundException;
import com.amx.amxlib.model.CustomerModel;
import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.CustomerVerificationType;
import com.amx.jax.customer.manager.CustomerContactVerificationManager;
import com.amx.jax.db.utils.EntityDtoUtil;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerContactVerification;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.CustomerVerification;
import com.amx.jax.dict.ContactType;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.customer.CustomerDto;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.services.AbstractService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.repository.CustomerVerificationRepository;
import com.amx.jax.userservice.repository.OnlineCustomerRepository;
import com.amx.jax.userservice.service.CustomerVerificationService;
import com.amx.jax.util.AmxDBConstants.Status;
import com.jax.amxlib.exception.jax.GlobaLException;

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
	@Autowired
	private CustomerVerificationRepository customerVerificationRepository;
	@Autowired
	private CustomerDao custDao;
	@Autowired
	private OnlineCustomerRepository onlineCustRepo;
	
	//changes done by Radhika
	public void sendEmailVerifyLinkOnReg(CustomerModel customerModel) {
		Customer customer = customerRepository.getActiveCustomerDetailsByCustomerId(customerModel.getCustomerId());
		customer.setEmailVerified(Status.N);
		customerRepository.save(customer);
		customer.setEmail(customerModel.getEmail());
		CustomerContactVerification customerContactVerification = customerContactVerificationManager.create(customer, ContactType.EMAIL);
				
		Email email = new Email();
		
		email.setITemplate(TemplatesMX.CONTACT_VERIFICATION_EMAIL);
		email.getModel().put("customer", EntityDtoUtil.entityToDto(customer, new CustomerDto()));
		email.getModel().put("link", customerContactVerification);
		email.addTo(customer.getEmail());
		email.setHtml(true);
		sendEmail(email);
		if(customer.getEmailVerified()==Status.N) {
			throw new GlobalException(JaxError.EMAIL_NOT_VERIFIED, "Email id is not verified . Please wait for 24 hrs");
		}
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
	
	
	
	public void validateEmailVerification(String identityId) {
		List<Customer> customer = customerRepository.findActiveCustomers(identityId);
		CustomerOnlineRegistration customerOnlineRegistration = onlineCustRepo.getLoginCustomersDeatilsById(identityId);
		if(customerOnlineRegistration.getStatus().equalsIgnoreCase("N")) {
			throw new GlobalException("Customer is not active Online");
		}
		
		else if(customer.get(0).getEmailVerified()==Status.Y) {
			CustomerVerification cv = customerVerificationService.getVerification(customer.get(0).getCustomerId(),
					CustomerVerificationType.EMAIL);
			logger.info("Customer Mail ------ : ");
			logger.info("Customer Data ------ : " +cv.toString());
			cv.setVerificationStatus(ConstantDocument.Yes);
			customerVerificationRepository.save(cv);
			customerOnlineRegistration.setStatus("Y");
			onlineCustRepo.save(customerOnlineRegistration);
		}else if(customer.get(0).getEmailVerified()==Status.N) {
			//@Radhika
			throw new GlobalException(JaxError.EMAIL_NOT_VERIFIED, "Email id is not verified . Please wait for 24 hrs");
 		}
		
	}
}
	
	