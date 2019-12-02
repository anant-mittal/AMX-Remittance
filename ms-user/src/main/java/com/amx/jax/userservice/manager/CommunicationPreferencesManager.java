package com.amx.jax.userservice.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dict.ContactType;
import com.amx.jax.dict.AmxEnums.CommunicationEvents;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.util.CommunicationPrefsUtil;
import com.amx.jax.util.CommunicationPrefsUtil.CommunicationPrefsResult;
import com.amx.utils.ArgUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CommunicationPreferencesManager {
	@Autowired
	MetaData metaData;

	@Autowired
	CustomerDao custDao;

	@Autowired
	CommunicationPrefsUtil communicationPrefsUtil;
	
	@Autowired
	CustomerRepository customerRepository;

	public void validateCommunicationPreferences(List<ContactType> channelList,
			CommunicationEvents communicationEvent,String identityInt) {
		Customer cust = null;
		if(ArgUtil.isEmpty(identityInt)) {
			cust = custDao.getActiveCustomerDetailsByCustomerId(metaData.getCustomerId());
		}else {
			cust = customerRepository.getActiveCustomerDetails(identityInt);
		}
		
		
		CommunicationPrefsResult communicationPrefsResult = communicationPrefsUtil.forCustomer(communicationEvent,
				cust);
		
		if(ArgUtil.isEmpty(channelList)) {
			boolean isSmsVerified = communicationPrefsResult.isSms();
			if (!isSmsVerified) {
				throw new GlobalException("Sms number is not verified");
			}
		}
		else{
			if(channelList.contains(ContactType.SMS_EMAIL)) {
				channelList.add(ContactType.EMAIL);
				channelList.add(ContactType.SMS);
			}
			for (ContactType channel : channelList) {
				if (ContactType.EMAIL.equals(channel)) {
					boolean isEmailVerified = communicationPrefsResult.isEmail();
					if (!isEmailVerified)
						throw new GlobalException(JaxError.EMAIL_NOT_VERIFIED,"Email id is not verified");
				} else if (ContactType.SMS.equals(channel)) {
					boolean isSmsVerified = communicationPrefsResult.isSms();
					if (!isSmsVerified) {
						throw new GlobalException(JaxError.SMS_NOT_VERIFIED,"Sms number is not verified");
					}
				} else if (ContactType.WHATSAPP.equals(channel)) {
					boolean isWhatsAppVerified = communicationPrefsResult.isWhatsApp();
					if (!isWhatsAppVerified) {
						throw new GlobalException(JaxError.WHATSAPP_NOT_VERIFIED,"Whatsapp number is not verified");
					}
				}
			}
		}
		
	}
}
