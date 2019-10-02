package com.amx.jax.userservice.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.AppContextUtil;
import com.amx.jax.JaxAuthCache;
import com.amx.jax.JaxAuthContext;
import com.amx.jax.JaxAuthMeta;
import com.amx.jax.JaxAuthMetaResp;
import com.amx.jax.dict.ContactType;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.CivilIdOtpModel;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.CryptoUtil;
import com.amx.utils.Random;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerAuthManager {

	@Autowired
	JaxNotificationService jaxNotificationService;
	@Autowired
	MetaData metaData;
	@Autowired
	UserService userService;
	@Autowired
	CryptoUtil cryptoUtil;
	@Autowired
	JaxAuthCache jaxAuthCache;

	private static final Logger log = LoggerFactory.getLogger(CustomerAuthManager.class);

	public void validateAndSendOtp(List<ContactType> contactTypes) {
		AppContextUtil.setContextId(getContextId());
		boolean isMotpRequired = false;
		boolean isEotpRequired = false;
		for (ContactType type : contactTypes) {
			switch (type) {
			case MOBILE:
			case SMS:
				if (JaxAuthContext.getMotp() == null) {
					isMotpRequired = true;
				} else {
					validateMotp();
				}
				break;
			case SMS_EMAIL:
				if (JaxAuthContext.getMotp() == null) {
					isMotpRequired = true;
				} else {
					validateMotp();
				}
				if (JaxAuthContext.getEotp() == null) {
					isEotpRequired = true;
				} else {
					validateEotp();
				}
				break;
			case EMAIL:
				if (JaxAuthContext.getEotp() == null) {
					isEotpRequired = true;
				} else {
					validateEotp();
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
				sendMotp(jaxAuthMetaResp);
			}
			if (isEotpRequired) {
				sendEotp(jaxAuthMetaResp);
			}
			ex.setMeta(jaxAuthMetaResp);
			throw ex;
		}
	}

	void sendEotp(JaxAuthMetaResp jaxAuthMetaResp) {
		String eOtp = Random.randomNumeric(6);
		String eOtpPrefix = Random.randomAlpha(3);
		String hashedeOtp = cryptoUtil.getHash(getContextId(), eOtp);
		JaxAuthMeta jaxAuthMeta = jaxAuthCache.getOrDefault(getContextId());
		jaxAuthMetaResp.seteOtpPrefix(eOtpPrefix);
		jaxAuthMeta.seteOtpHash(hashedeOtp);
		jaxAuthCache.saveJaxAuthMeta(jaxAuthMeta);
		CivilIdOtpModel model = new CivilIdOtpModel();
		model.seteOtp(eOtp);
		model.seteOtpPrefix(eOtpPrefix);
		jaxNotificationService.sendOtpEmail(userService.getPersonInfo(metaData.getCustomerId()), model);
	}

	void sendMotp(JaxAuthMetaResp jaxAuthMetaResp) {
		String mOtp = Random.randomNumeric(6);
		String mOtpPrefix = Random.randomAlpha(3);
		String hashedmOtp = cryptoUtil.getHash(getContextId(), mOtp);
		JaxAuthMeta jaxAuthMeta = jaxAuthCache.getOrDefault(getContextId());
		jaxAuthMetaResp.setmOtpPrefix(mOtpPrefix);
		jaxAuthMeta.setmOtpHash(hashedmOtp);
		jaxAuthCache.saveJaxAuthMeta(jaxAuthMeta);
		CivilIdOtpModel model = new CivilIdOtpModel();
		model.setmOtp(mOtp);
		model.setmOtpPrefix(mOtpPrefix);
		jaxNotificationService.sendOtpSms(userService.getPersonInfo(metaData.getCustomerId()), model);
	}

	public String getContextId() {
		return "CUSTOMER_AUTH_" + metaData.getCustomerId();
	}

	public void validateEotp() {
		String eOtp = JaxAuthContext.getEotp();
		JaxAuthMeta jaxAuthMeta = jaxAuthCache.getJaxAuthMeta();
		String eotpHash = cryptoUtil.getHash(getContextId(), eOtp);
		if (!eotpHash.equals(jaxAuthMeta.geteOtpHash())) {
			throw new GlobalException(JaxError.INVALID_OTP, "invalid eOtp");
		}
	}

	public void validateMotp() {
		String mOtp = JaxAuthContext.getMotp();
		JaxAuthMeta jaxAuthMeta = jaxAuthCache.getJaxAuthMeta();
		String motpHash = cryptoUtil.getHash(getContextId(), mOtp);
		if (!motpHash.equals(jaxAuthMeta.getmOtpHash())) {
			throw new GlobalException(JaxError.INVALID_OTP, "invalid mOtp");
		}
	}
}
