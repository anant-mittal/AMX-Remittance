package com.amx.jax.userservice.manager;

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
import com.amx.jax.JaxAuthContext;
import com.amx.jax.JaxAuthMetaResp;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dict.ContactType;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.userservice.service.UserValidationService;
import com.amx.jax.util.CryptoUtil;
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

	private static final Logger log = LoggerFactory.getLogger(CustomerDBAuthManager.class);

	public void validateAndSendOtp(String identityInt, String resetPwd) {
		List<ContactType> contactTypes = null;
		// contact type list check here....
		
		CustomerOnlineRegistration onlineCust = custDao.getOnlineCustByCustomerId(metaData.getCustomerId());
		Customer customer = custDao.getCustById(metaData.getCustomerId());
				
		boolean isMotpRequired = false;
		boolean isEotpRequired = false;
		for (ContactType type : contactTypes) {
			switch (type) {
			case MOBILE:
			case SMS:
				if (JaxAuthContext.getMotp() == null) {
					isMotpRequired = true;
				} else {
					validateMotp(onlineCust, customer);
				}
				break;
			case SMS_EMAIL:
				if (JaxAuthContext.getMotp() == null) {
					isMotpRequired = true;
				} else {
					validateMotp(onlineCust, customer);
				}
				if (JaxAuthContext.getEotp() == null) {
					isEotpRequired = true;
				} else {
					validateEotp(onlineCust, customer);
				}
				break;
			case EMAIL:
				if (JaxAuthContext.getEotp() == null) {
					isEotpRequired = true;
				} else {
					validateEotp(onlineCust, customer);
				}
				break;

			default:
				break;
			}
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
	}

	void sendEotp(JaxAuthMetaResp jaxAuthMetaResp, CustomerOnlineRegistration onlineCust, Customer customer) {
		String eOtp = Random.randomNumeric(6);
		String eOtpPrefix = Random.randomAlpha(3);
		String hashedeOtp = cryptoUtil.getHash(customer.getIdentityInt(), eOtp);
		jaxAuthMetaResp.seteOtpPrefix(eOtpPrefix);

		CivilIdOtpModel model = new CivilIdOtpModel();
		model.seteOtp(eOtp);
		model.seteOtpPrefix(eOtpPrefix);
		model.setHashedeOtp(hashedeOtp);

		onlineCust.setEmailToken(model.getHashedeOtp());
		custDao.saveOnlineCustomer(onlineCust);

		jaxNotificationService.sendOtpEmail(userService.getPersonInfo(metaData.getCustomerId()), model);
	}

	private void sendMotp(JaxAuthMetaResp jaxAuthMetaResp, CustomerOnlineRegistration onlineCust, Customer customer) {
		String mOtp = Random.randomNumeric(6);
		String mOtpPrefix = Random.randomAlpha(3);
		String hashedmOtp = cryptoUtil.getHash(customer.getIdentityInt(), mOtp);
		jaxAuthMetaResp.setmOtpPrefix(mOtpPrefix);

		CivilIdOtpModel model = new CivilIdOtpModel();
		model.setmOtp(mOtp);
		model.setmOtpPrefix(mOtpPrefix);
		model.setHashedmOtp(hashedmOtp);

		onlineCust.setSmsToken(model.getHashedmOtp());
		custDao.saveOnlineCustomer(onlineCust);

		jaxNotificationService.sendOtpSms(userService.getPersonInfo(metaData.getCustomerId()), model);
	}

	public void validateEotp(CustomerOnlineRegistration onlineCust, Customer customer) {
		String eOtp = JaxAuthContext.getEotp();
		String etokenHash = onlineCust.getEmailToken();
		String eOtpHash = null;
		if (StringUtils.isNotBlank(eOtp)) {
			eOtpHash = cryptoUtil.getHash(customer.getIdentityInt(), eOtp);
		}
		if (eOtpHash != null && etokenHash != null && !eOtpHash.equals(etokenHash)) {
			userValidationService.incrementLockCount(onlineCust);
			throw new GlobalException(JaxError.INVALID_OTP, "invalid eOtp");
		}
	}

	public void validateMotp(CustomerOnlineRegistration onlineCust, Customer customer) {
		String mOtp = JaxAuthContext.getEotp();
		String mtokenHash = onlineCust.getSmsToken();
		String mOtpHash = null;
		if (StringUtils.isNotBlank(mOtp)) {
			mOtpHash = cryptoUtil.getHash(customer.getIdentityInt(), mOtp);
		}
		if (mOtpHash != null && mtokenHash != null && !mOtpHash.equals(mtokenHash)) {
			userValidationService.incrementLockCount(onlineCust);
			throw new GlobalException(JaxError.INVALID_OTP, "invalid mOtp");
		}
	}

}
