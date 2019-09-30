package com.amx.jax.userservice.manager;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.jax.JaxAuthContext;
import com.amx.jax.JaxAuthMetaResp;
import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.ViewOnlineCurrency;
import com.amx.jax.dict.ContactType;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.customer.CustomerCommunicationChannel;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.service.CommunicationChannelContactService;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.userservice.service.UserValidationService;
import com.amx.jax.util.CryptoUtil;
import com.amx.utils.ArgUtil;
import com.amx.utils.Random;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerDBAuthManager {

	@Autowired
	JaxNotificationService jaxNotificationService;

	@Autowired
	MetaData metaData;

	@Autowired
	UserService userService;

	@Autowired
	CryptoUtil cryptoUtil;

	@Autowired
	private CustomerDao custDao;

	@Autowired
	private UserValidationService userValidationService;

	@Autowired
	CommunicationChannelContactService communicationChannelContactService;
	
	@Autowired
	OtpSettings otpSettings;

	private static final Logger log = LoggerFactory.getLogger(CustomerDBAuthManager.class);

	public CustomerModel validateAndSendOtp(String identityInt) {
		List<Customer> customers = userService.getCustomerByIdentityInt(identityInt);
		Customer customerVal = userValidationService.validateCustomerForDuplicateRecords(customers);
		BigDecimal customerId = customerVal.getCustomerId();

		CustomerOnlineRegistration onlineCust = custDao.getOnlineCustByCustomerId(customerId);
		Customer customer = custDao.getCustById(customerId);
		
		if(onlineCust != null) {
			if (onlineCust.getLockCnt() != null) {
				int lockCnt = onlineCust.getLockCnt().intValue();
				final Integer MAX_OTP_ATTEMPTS = otpSettings.getMaxValidateOtpAttempts();
				if (lockCnt >= MAX_OTP_ATTEMPTS) {
					throw new GlobalException(JaxError.USER_LOGIN_ATTEMPT_EXCEEDED,
							"Customer is locked. No of attempts:- " + lockCnt);
				}
			}
		}

		ContactType contactType = JaxAuthContext.getContactType();

		if (contactType == null) {

			// send list of contact types available for customer
			List<CustomerCommunicationChannel> communicationChannelContact = communicationChannelContactService
					.getCustomerCommunicationChannels(identityInt);
			
			Iterator<CustomerCommunicationChannel> itr = communicationChannelContact.iterator();
			if (!communicationChannelContact.isEmpty()) {
				while (itr.hasNext()) {
					ContactType name = itr.next().getContactType();
					log.info("CONTACT TYPE NAME ---> "+name);
					if(ContactType.WHATSAPP.equals(name)) {
						itr.remove();
					}
				}
			}

			// set communication channel in meta of exception then throw exception
			GlobalException ex = new GlobalException(JaxError.CONTACT_TYPE_REQUIRED, "Contact Type is missing");
			ex.setMeta(communicationChannelContact);
			throw ex;
		}

		boolean isMotpRequired = false;
		boolean isEotpRequired = false;
		
		String mOtp = JaxAuthContext.getMotpOrOtp();
		String eOtp = JaxAuthContext.getEotpOrOtp();

		switch (contactType) {
		case MOBILE:
		case SMS:
			if (ArgUtil.isEmpty(mOtp)) {
				isMotpRequired = true;
			} else {
				validateMotp(onlineCust, customer);
			}
			break;
		case SMS_EMAIL:
			if (ArgUtil.isEmpty(mOtp)) {
				isMotpRequired = true;
			} else {
				validateMotp(onlineCust, customer);
			}
			if (ArgUtil.isEmpty(eOtp)) {
				isEotpRequired = true;
			} else {
				validateEotp(onlineCust, customer);
			}
			break;
		case EMAIL:
			if (ArgUtil.isEmpty(eOtp)) {
				isEotpRequired = true;
			} else {
				validateEotp(onlineCust, customer);
			}
			break;

		default:
			break;
		}

		GlobalException ex = null;
		if (isMotpRequired && isEotpRequired) {
			ex = new GlobalException(JaxError.DOTP_REQUIRED, "e and m otp required");
		}
		if (isMotpRequired) {
			ex = new GlobalException(JaxError.MOTP_REQUIRED, "m otp required");
		}
		if (isEotpRequired) {
			ex = new GlobalException(JaxError.EOTP_REQUIRED, "e otp required");
		}
		if (ex != null) {
			// send otp validations here
			userValidationService.validateCustomerContactForSendOtp(Arrays.asList(contactType), customer);
			userService.validateTokenExpiryTime(onlineCust);
			userValidationService.validateTokenSentCount(onlineCust);
			userService.incrementTokenSentCount(onlineCust);

			log.debug("sending otp of types:- {}, {}", isMotpRequired, isEotpRequired);
			JaxAuthMetaResp jaxAuthMetaResp = new JaxAuthMetaResp();
			if (isMotpRequired) {
				sendMotp(jaxAuthMetaResp, onlineCust, customer);
			}
			if (isEotpRequired) {
				sendEotp(jaxAuthMetaResp, onlineCust, customer);
			}
			ex.setMeta(jaxAuthMetaResp);
			throw ex;
		}
		
		return userService.convert(onlineCust);
	}

	void sendEotp(JaxAuthMetaResp jaxAuthMetaResp, CustomerOnlineRegistration onlineCust, Customer customer) {
		String eOtp = Random.randomNumeric(6);
		String eOtpPrefix = Random.randomAlpha(3);
		String hashedeOtp = cryptoUtil.getHash(customer.getIdentityInt(), eOtp);
		jaxAuthMetaResp.seteOtpPrefix(eOtpPrefix);
		jaxAuthMetaResp.setOtpPrefix(eOtpPrefix);

		CivilIdOtpModel model = new CivilIdOtpModel();
		model.seteOtp(eOtp);
		model.seteOtpPrefix(eOtpPrefix);
		model.setHashedeOtp(hashedeOtp);

		onlineCust.setEmailToken(model.getHashedeOtp());
		custDao.saveOnlineCustomer(onlineCust);

		jaxNotificationService.sendOtpEmail(userService.getPersonInfo(customer.getCustomerId()), model);
	}

	private void sendMotp(JaxAuthMetaResp jaxAuthMetaResp, CustomerOnlineRegistration onlineCust, Customer customer) {
		String mOtp = Random.randomNumeric(6);
		String mOtpPrefix = Random.randomAlpha(3);
		String hashedmOtp = cryptoUtil.getHash(customer.getIdentityInt(), mOtp);
		jaxAuthMetaResp.setmOtpPrefix(mOtpPrefix);
		jaxAuthMetaResp.setOtpPrefix(mOtpPrefix);

		CivilIdOtpModel model = new CivilIdOtpModel();
		model.setmOtp(mOtp);
		model.setmOtpPrefix(mOtpPrefix);
		model.setHashedmOtp(hashedmOtp);

		onlineCust.setSmsToken(model.getHashedmOtp());
		custDao.saveOnlineCustomer(onlineCust);

		jaxNotificationService.sendOtpSms(userService.getPersonInfo(customer.getCustomerId()), model);
	}

	public void validateEotp(CustomerOnlineRegistration onlineCust, Customer customer) {
		String eOtp = JaxAuthContext.getEotpOrOtp();
		String etokenHash = onlineCust.getEmailToken();
		String eOtpHash = null;
		if (StringUtils.isNotBlank(eOtp)) {
			eOtpHash = cryptoUtil.getHash(customer.getIdentityInt(), eOtp);
		}
		if (eOtpHash != null && etokenHash != null && !eOtpHash.equals(etokenHash)) {
			userValidationService.incrementLockCount(onlineCust);
			throw new GlobalException(JaxError.INVALID_OTP, "invalid eOtp");
		}
		// unlock method here
		userService.unlockCustomer(onlineCust);
		custDao.saveOnlineCustomer(onlineCust);
	}

	public void validateMotp(CustomerOnlineRegistration onlineCust, Customer customer) {
		String mOtp = JaxAuthContext.getMotpOrOtp(); 
		String mtokenHash = onlineCust.getSmsToken();
		String mOtpHash = null;
		if (StringUtils.isNotBlank(mOtp)) {
			mOtpHash = cryptoUtil.getHash(customer.getIdentityInt(), mOtp);
		}
		if (mOtpHash != null && mtokenHash != null && !mOtpHash.equals(mtokenHash)) {
			userValidationService.incrementLockCount(onlineCust);
			throw new GlobalException(JaxError.INVALID_OTP, "invalid mOtp");
		}
		// unlock method here
		userService.unlockCustomer(onlineCust);
		custDao.saveOnlineCustomer(onlineCust);
	}

}
