package com.amx.jax.userservice.manager;

import static com.amx.amxlib.constant.NotificationConstants.RESP_DATA_KEY;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dict.AmxEnums.CommunicationEvents;
import com.amx.jax.dict.Communicatable;
import com.amx.jax.dict.ContactType;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.util.CommunicationPrefsUtil;
import com.amx.jax.util.CommunicationPrefsUtil.CommunicationPrefsResult;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CommunicationPreferencesManager {
	
	Logger logger = LoggerFactory.getLogger(CommunicationPreferencesManager.class);
	
	@Autowired
	MetaData metaData;

	@Autowired
	CustomerDao custDao;

	@Autowired
	CommunicationPrefsUtil communicationPrefsUtil;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	PushNotifyClient pushNotifyClient;
	
	public void validateCommunicationPreferences(List<ContactType> channelList, CommunicationEvents communicationEvent,
			String identityInt) {
		Customer cust = null;
		
		if(ArgUtil.isEmpty(identityInt)) {
			cust = custDao.getActiveCustomerDetailsByCustomerId(metaData.getCustomerId());
		}else {
			cust = customerRepository.getActiveCustomerDetails(identityInt);
		}
		
		logger.debug("Customer object value is "+cust.toString());
		CommunicationPrefsResult communicationPrefsResult = communicationPrefsUtil.forCustomer(communicationEvent,
				cust);
		logger.debug("Communication result for sms is "+communicationPrefsResult.isSms());
		
		if(ArgUtil.isEmpty(channelList)) {
			boolean isSmsVerified = cust.canSendMobile();
			if (!isSmsVerified && metaData.getChannel().equals(JaxChannel.ONLINE)) {
				throw new GlobalException(JaxError.SMS_NOT_VERIFIED,
						"Your registered mobile number is not verified. Please visit the branch to complete verification.");
			}else if(!isSmsVerified && metaData.getChannel().equals(JaxChannel.BRANCH)) {
				throw new GlobalException(JaxError.SMS_NOT_VERIFIED,
						"Please call customer to verify his registered mobile number.");
			}
		} else {
			if(channelList.contains(ContactType.SMS_EMAIL)) {
				channelList.add(ContactType.EMAIL);
				channelList.add(ContactType.SMS);
			}
			for (ContactType channel : channelList) {
				if (ContactType.EMAIL.equals(channel)) {
					boolean isEmailVerified = cust.canSendEmail();
					if (!isEmailVerified && metaData.getChannel().equals(JaxChannel.ONLINE)) {
						throw new GlobalException(JaxError.EMAIL_NOT_VERIFIED,
								"Your registered email  is not verified. Please complete verification steps for successful verification.");
					}else if(!isEmailVerified && metaData.getChannel().equals(JaxChannel.BRANCH)){
						sendPushNotification(channel,cust);
						throw new GlobalException(JaxError.EMAIL_NOT_VERIFIED,
								"Please call customer to verify his registered email.");
					}
						
				} 
				
				else if (ContactType.SMS.equals(channel)) {
					boolean isSmsVerified = cust.canSendMobile();
					if (!isSmsVerified && metaData.getChannel().equals(JaxChannel.ONLINE)) {
						throw new GlobalException(JaxError.SMS_NOT_VERIFIED,
								"Your registered mobile number is not verified. Please visit the branch to complete verification.");
					}else if(!isSmsVerified && metaData.getChannel().equals(JaxChannel.BRANCH)) {
						sendPushNotification(channel,cust);
						throw new GlobalException(JaxError.SMS_NOT_VERIFIED,
								"Please call customer to verify his registered mobile number.");
					}
					
				} else if (ContactType.WHATSAPP.equals(channel)) {
					boolean isWhatsAppVerified = cust.canSendWhatsApp();
					if (!isWhatsAppVerified && metaData.getChannel().equals(JaxChannel.ONLINE)) {
						throw new GlobalException(JaxError.WHATSAPP_NOT_VERIFIED,
								"Your registered whatsapp number is not verified. Please visit the branch to complete verification.");
					}else if(!isWhatsAppVerified && metaData.getChannel().equals(JaxChannel.BRANCH)) {
						sendPushNotification(channel,cust);
						throw new GlobalException(JaxError.WHATSAPP_NOT_VERIFIED,
								"Please call customer to verify his registered whatsapp number.");
					}
				}
			}
		}
		
	}

	private void sendPushNotification(ContactType channel,Customer customer) {
		logger.debug("PushNotify for fx order");
		PushMessage pushMessage = new PushMessage();
		pushMessage.setITemplate(TemplatesMX.VERIFICATION_NOTIFY);
		pushMessage.addToUser(customer.getCustomerId());
		pushMessage.getModel().put(RESP_DATA_KEY, channel);
		logger.debug("Data for push notif "+JsonUtil.toJson(pushMessage));
		pushNotifyClient.send(pushMessage);
	}

	// TODO:- @Anant to implement these methods

	/**
	 * 
	 * 
	 * 
	 * @param customerId
	 * @param model
	 * @param communicationEvent
	 * @param templateMX
	 * @param contactTypes       - channels to try for notifications, if null/empty
	 *                           attempt will be made on all channels
	 * @return - if notification has been sent to specified contact or not
	 */
	public boolean sendMessage(BigDecimal customerId, Object model, CommunicationEvents communicationEvent,
			TemplatesMX templateMX, ContactType... contactTypes) {
		return false;
	}

	// TODO:- @Anant to implement these methods
	public boolean sendMessage(String indetnityInt, Object model, CommunicationEvents communicationEvent,
			TemplatesMX templateMX, ContactType... contactTypes) {
		return false;
	}

	// TODO:- @Anant to implement these methods
	public boolean sendMessage(Communicatable commCustomer, Object model, CommunicationEvents communicationEvent,
			TemplatesMX templateMX, ContactType... contactTypes) {
		return false;
	}

}
