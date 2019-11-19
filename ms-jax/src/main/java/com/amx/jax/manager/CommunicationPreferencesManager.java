package com.amx.jax.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dict.AmxEnums.CommunicationEvents;
import com.amx.jax.dict.ContactType;
import com.amx.jax.meta.MetaData;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.util.CommunicationPrefsUtil;
import com.amx.jax.util.CommunicationPrefsUtil.CommunicationPrefsResult;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CommunicationPreferencesManager {
	@Autowired
	MetaData metaData;

	@Autowired
	CustomerDao custDao;

	@Autowired
	CommunicationPrefsUtil communicationPrefsUtil;

	public void validateCommunicationPreferences(List<ContactType> channel) {
		Customer cust = custDao.getActiveCustomerDetailsByCustomerId(metaData.getCustomerId());

		CommunicationPrefsResult communicationPrefsResult = communicationPrefsUtil
				.forCustomer(CommunicationEvents.ADD_BENEFICIARY, cust);

		boolean isEmailVerified = communicationPrefsResult.isEmail();
		boolean isSmsVerified = communicationPrefsResult.isSms();
		boolean isWhatsappVerified = communicationPrefsResult.isWhatsApp();

		if (!isEmailVerified && !isSmsVerified && !isWhatsappVerified) {
			throw new GlobalException("Email,Sms,whatsapp are not verified");
		}
		if (!isEmailVerified && !isSmsVerified && isWhatsappVerified) {
			throw new GlobalException("Email,Sms are not verified");
		}
		if (isEmailVerified && !isSmsVerified && !isWhatsappVerified) {
			throw new GlobalException("Sms,Whatsapp are not verified");
		}
		if (!isEmailVerified && isSmsVerified && !isWhatsappVerified) {
			throw new GlobalException("Email,Whatsapp are not verified");
		}

		if (!communicationPrefsResult.isEmail()) {
			throw new GlobalException("Email is not verified");
		}
		if (!communicationPrefsResult.isSms()) {
			throw new GlobalException("Sms is not verified");
		}
		if (!communicationPrefsResult.isWhatsApp()) {
			throw new GlobalException("Whatsapp is not verified");
		}
	}
}
