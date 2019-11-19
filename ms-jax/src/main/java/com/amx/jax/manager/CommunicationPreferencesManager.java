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

	public void validateCommunicationPreferences(List<ContactType> channelList) {
		Customer cust = custDao.getActiveCustomerDetailsByCustomerId(metaData.getCustomerId());

		CommunicationPrefsResult communicationPrefsResult = communicationPrefsUtil
				.forCustomer(CommunicationEvents.ADD_BENEFICIARY, cust);

		for (ContactType channel : channelList) {
			if (ContactType.EMAIL.equals(channel)) {
				boolean isEmailVerified = communicationPrefsResult.isEmail();
				if (!isEmailVerified)
					throw new GlobalException("Email id is not verified");
			} else if (ContactType.SMS.equals(channel)) {
				boolean isSmsVerified = communicationPrefsResult.isSms();
				if (!isSmsVerified) {
					throw new GlobalException("Sms number is not verified");
				}
			} else if (ContactType.WHATSAPP.equals(channel)) {
				boolean isWhatsAppVerified = communicationPrefsResult.isWhatsApp();
				if (!isWhatsAppVerified) {
					throw new GlobalException("Whatsapp number is not verified");
				}
			}
		}
	}
}
