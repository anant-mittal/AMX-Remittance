package com.amx.jax.userservice.manager;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.CustomerModel;
import com.amx.jax.JaxAuthContext;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.JaxApiFlow;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dict.ContactType;
import com.amx.jax.dict.AmxEnums.CommunicationEvents;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.SecurityQuestionModel;
import com.amx.jax.services.JaxDBService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.repository.OnlineCustomerRepository;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.userservice.service.UserValidationService;
import com.amx.jax.util.CryptoUtil;

@Component
public class OnlineCustomerManager {

	@Autowired
	CustomerRegistrationManager customerRegistrationManager;
	@Autowired
	CustomerDao custDao;
	@Autowired
	MetaData metaData;
	@Autowired
	UserService userService;
	@Autowired
	UserValidationService userValidationService;
	@Autowired
	CustomerAuthManager customerAuthManager;
	@Autowired
	CustomerDBAuthManager customerDBAuthManager;
	@Autowired
	JaxDBService jaxDBService;
	@Autowired
	private CryptoUtil cryptoUtil;
	
	private static final Logger log = LoggerFactory.getLogger(OnlineCustomerManager.class);

	@Autowired
	UserContactVerificationManager userContactVerificationManager;
	
	@Autowired
	OnlineCustomerRepository onlineCustomerRepository;
	
	@Autowired
	CommunicationPreferencesManager communicationPreferencesManager;

	public void saveCustomerSecQuestions(List<SecurityQuestionModel> securityQuestions) {
		CustomerOnlineRegistration customerOnlineRegistration = custDao
				.getOnlineCustByCustomerId(metaData.getCustomerId());
		if (customerOnlineRegistration == null) {
			throw new GlobalException("Online customer not found");
		}
		Customer customer = custDao.getCustById(metaData.getCustomerId());
		// send otp when update is needed
		if (customerOnlineRegistration.getSecurityQuestion1() != null
				&& customerOnlineRegistration.getSecurityAnswer1() != null) {
			userValidationService.validateCustomerLockCount(customerOnlineRegistration);
			try {
				// signifies that it is send otp flow
				if (JaxAuthContext.getMotp() == null) {
					userValidationService.validateCustomerContactForSendOtp(Arrays.asList(ContactType.SMS), customer);
					//communicationPreferencesManager.validateCommunicationPreferences(null,CommunicationEvents.UPDATE_SECQUE,null);
					userService.validateTokenExpiryTime(customerOnlineRegistration);
					userValidationService.validateTokenSentCount(customerOnlineRegistration);
					userService.incrementTokenSentCount(customerOnlineRegistration);
				}
				customerAuthManager.validateAndSendOtp(Arrays.asList(ContactType.SMS));
				// signifies that it is validate otp flow
				if (JaxAuthContext.getMotp() != null) {
					userService.unlockCustomer(customerOnlineRegistration);
					userContactVerificationManager.setContactVerified(customer, JaxAuthContext.getMotp(), null, null);
				}
			} catch (GlobalException ex) {
				if (JaxError.INVALID_OTP.equals(ex.getError())) {
					userValidationService.incrementLockCount(customerOnlineRegistration);
				}
				throw ex;
			}
		}
		userService.simplifyAnswers(securityQuestions);
		custDao.setSecurityQuestions(securityQuestions, customerOnlineRegistration);
		custDao.saveOnlineCustomer(customerOnlineRegistration);
	}

	public void doSignUpValidations(String identityInt) {
		List<Customer> customers = userValidationService.validateNonActiveOrNonRegisteredCustomerStatus(identityInt,
				JaxApiFlow.SIGNUP_ONLINE);
		Customer customer = customers.get(0);
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(customer.getCustomerId());
		
		if (onlineCustomer != null && ConstantDocument.Yes.equals(onlineCustomer.getStatus())) {
			throw new GlobalException(JaxError.USER_ALREADY_ACTIVE,
					"You are already registered with us. Please login.");
		}
		userValidationService.validateCustIdProofs(customer.getCustomerId());
		userValidationService.validateBlackListedCustomerForLogin(customer);
		userValidationService.validateCustomerVerification(customer.getCustomerId());
	}

	public CustomerModel validateCustomerLoginOtp(String identityInt) {
		return customerDBAuthManager.validateAndSendOtp(identityInt);
	}

	public void updatePassword(String identityInt, String resetPwd) {
		// reset password
		List<Customer> customers = userService.getCustomerByIdentityInt(identityInt);
		Customer customerVal = userValidationService.validateCustomerForDuplicateRecords(customers);
		BigDecimal customerId = customerVal.getCustomerId();
		
		CustomerOnlineRegistration onlineCust = custDao.getOnlineCustomerByCustomerId(customerId);
		String userId = onlineCust.getUserName();
		if (resetPwd != null) {
			onlineCust.setPassword(cryptoUtil.getHash(userId, resetPwd));
			onlineCustomerRepository.save(onlineCust);
		}else {
			throw new GlobalException(JaxError.UPDATE_PWD_REQUIRED, "Please enter Password to reset");
		}
	}	

	public CustomerOnlineRegistration getOnlineCustomerByCustomerId(BigDecimal customerId) {
		return custDao.getOnlineCustByCustomerId(customerId);
	}
	
	public void lockCustomer(BigDecimal customerId) {
		log.info("locking customer");
		CustomerOnlineRegistration onlineCustomer = validateActiveOnlineCustomer(customerId);
		onlineCustomer.setLockDt(new Date());
		onlineCustomer.setUpdatedBy(jaxDBService.getCreatedOrUpdatedBy());
		custDao.saveOnlineCustomer(onlineCustomer);
	}

	public void unlockCustomer(BigDecimal customerId) {
		log.info("unlocking customer");
		CustomerOnlineRegistration onlineCustomer = validateActiveOnlineCustomer(customerId);
		onlineCustomer.setLockDt(null);
		onlineCustomer.setUnlockBy(jaxDBService.getCreatedOrUpdatedBy());
		onlineCustomer.setUnlockDt(new Date());
		onlineCustomer.setUnLockIp(metaData.getDeviceIp());
		custDao.saveOnlineCustomer(onlineCustomer);
	}
	
	public CustomerOnlineRegistration validateActiveOnlineCustomer(BigDecimal customerId) {
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(customerId);
		if (onlineCustomer == null) {
			throw new GlobalException("Online customer not registered");
		}
		if (!ConstantDocument.Yes.equals(onlineCustomer.getStatus())) {
			throw new GlobalException("Online customer not active");
		}
		return onlineCustomer;
	}
}
