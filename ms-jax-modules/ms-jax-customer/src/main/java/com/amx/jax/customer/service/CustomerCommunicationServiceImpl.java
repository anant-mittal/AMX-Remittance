package com.amx.jax.customer.service;

import static com.amx.amxlib.constant.NotificationConstants.RESP_DATA_KEY;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dict.AmxEnums.CommunicationEvents;
import com.amx.jax.dict.ContactType;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.client.WhatsAppClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.util.CommunicationPrefsUtil;
import com.amx.jax.util.CommunicationPrefsUtil.CommunicationPrefsResult;
import com.amx.utils.ArgUtil;
import com.amx.utils.CollectionUtil;
import com.amx.utils.JsonUtil;

@Component
public class CustomerCommunicationServiceImpl implements CustomerCommunicationService {

	Logger logger = LoggerFactory.getLogger(CustomerCommunicationServiceImpl.class);

	@Autowired
	MetaData metaData;

	@Autowired
	CustomerDao custDao;

	@Autowired
	CommunicationPrefsUtil communicationPrefsUtil;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	private PostManService postManService;

	@Autowired
	private PushNotifyClient pushNotifyClient;

	@Autowired
	private WhatsAppClient whatsAppClient;

	public void validateCommunicationPreferences(List<ContactType> channelList,
			CommunicationEvents communicationEvent, String identityInt) {
		Customer cust = null;

		if (ArgUtil.isEmpty(identityInt)) {
			cust = custDao.getActiveCustomerDetailsByCustomerId(metaData.getCustomerId());
		} else {
			cust = customerRepository.getActiveCustomerDetails(identityInt);
		}

		logger.debug("Customer object value is " + cust.toString());
		CommunicationPrefsResult communicationPrefsResult = communicationPrefsUtil.forCustomer(communicationEvent,
				cust);
		logger.debug("Communication result for sms is " + communicationPrefsResult.isSms());

		if (ArgUtil.isEmpty(channelList)) {
			boolean isSmsVerified = cust.canSendMobile();
			if (!isSmsVerified && metaData.getChannel().equals(JaxChannel.ONLINE)) {
				throw new GlobalException(JaxError.SMS_NOT_VERIFIED,
						"Your registered mobile number is not verified. Please visit the branch to complete verification.");
			} else if (!isSmsVerified && metaData.getChannel().equals(JaxChannel.BRANCH)) {
				throw new GlobalException(JaxError.SMS_NOT_VERIFIED,
						"Please call customer to verify his registered mobile number.");
			}
		} else {
			if (channelList.contains(ContactType.SMS_EMAIL)) {
				channelList.add(ContactType.EMAIL);
				channelList.add(ContactType.SMS);
			}
			for (ContactType channel : channelList) {
				if (ContactType.EMAIL.equals(channel)) {
					boolean isEmailVerified = cust.canSendEmail();
					if (!isEmailVerified && metaData.getChannel().equals(JaxChannel.ONLINE)) {
						throw new GlobalException(JaxError.EMAIL_NOT_VERIFIED,
								"Your registered email  is not verified. Please complete verification steps for successful verification.");
					} else if (!isEmailVerified && metaData.getChannel().equals(JaxChannel.BRANCH)) {
						sendPushNotification(channel, cust);
						throw new GlobalException(JaxError.EMAIL_NOT_VERIFIED,
								"Please call customer to verify his registered email.");
					}

				}

				else if (ContactType.SMS.equals(channel)) {
					boolean isSmsVerified = cust.canSendMobile();
					if (!isSmsVerified && metaData.getChannel().equals(JaxChannel.ONLINE)) {
						throw new GlobalException(JaxError.SMS_NOT_VERIFIED,
								"Your registered mobile number is not verified. Please visit the branch to complete verification.");
					} else if (!isSmsVerified && metaData.getChannel().equals(JaxChannel.BRANCH)) {
						sendPushNotification(channel, cust);
						throw new GlobalException(JaxError.SMS_NOT_VERIFIED,
								"Please call customer to verify his registered mobile number.");
					}

				} else if (ContactType.WHATSAPP.equals(channel)) {
					boolean isWhatsAppVerified = cust.canSendWhatsApp();
					if (!isWhatsAppVerified && metaData.getChannel().equals(JaxChannel.ONLINE)) {
						throw new GlobalException(JaxError.WHATSAPP_NOT_VERIFIED,
								"Your registered whatsapp number is not verified. Please visit the branch to complete verification.");
					} else if (!isWhatsAppVerified && metaData.getChannel().equals(JaxChannel.BRANCH)) {
						sendPushNotification(channel, cust);
						throw new GlobalException(JaxError.WHATSAPP_NOT_VERIFIED,
								"Please call customer to verify his registered whatsapp number.");
					}
				}
			}
		}

	}

	private void sendPushNotification(ContactType channel, Customer cust) {
		logger.debug("PushNotify for fx order");
		PushMessage pushMessage = new PushMessage();
		pushMessage.setITemplate(TemplatesMX.VERIFICATION_NOTIFY);
		pushMessage.addToUser(cust.getCustomerId());
		pushMessage.getModel().put(RESP_DATA_KEY, channel);
		logger.debug("Data for push notif " + JsonUtil.toJson(pushMessage));
		pushNotifyClient.send(pushMessage);
	}

	private ContactType[] getContactTypes(CommunicationEvents communicationEvent) {
		switch (communicationEvent) {
		case TRNX_CREATION_NOTIFY:
			return CollectionUtil.getArray(ContactType.SMS);
		default:
			return CollectionUtil.getArray(ContactType.EMAIL,
					ContactType.SMS,
					ContactType.WHATSAPP,
					ContactType.FBPUSH);

		}
	}

	public CommunicationPrefsResult getCommunicationPrefResult(
			CommunicationEvents communicationEvent, Customer commCustomer, ContactType... contactTypes) {
		if (contactTypes.length == 0) {
			contactTypes = getContactTypes(communicationEvent);
		}

		Set<ContactType> contactTypesLists = CollectionUtil.getSet(contactTypes);
		if (contactTypesLists.contains(ContactType.SMS_EMAIL)) {
			contactTypesLists.add(ContactType.EMAIL);
			contactTypesLists.add(ContactType.SMS);
		}

		CommunicationPrefsResult communicationFlowPrefs = communicationPrefsUtil.forCustomer(communicationEvent,
				commCustomer);

		communicationFlowPrefs
				.setEmail(contactTypesLists.contains(ContactType.EMAIL) && communicationFlowPrefs.isEmail());

		communicationFlowPrefs
				.setSms(contactTypesLists.contains(ContactType.SMS) && communicationFlowPrefs.isSms());

		communicationFlowPrefs
				.setWhatsApp(contactTypesLists.contains(ContactType.WHATSAPP) && communicationFlowPrefs.isWhatsApp());

		communicationFlowPrefs
				.setPushNotify(contactTypesLists.contains(ContactType.FBPUSH) && communicationFlowPrefs.isPushNotify());

		return communicationFlowPrefs;
	}

	public CommunicationPrefsResult getCommunicationPrefResult(BigDecimal customerId,
			CommunicationEvents communicationEvent, ContactType... contactTypes) {
		if (!ArgUtil.isEmpty(customerId)) {
			Customer cust = custDao.getActiveCustomerDetailsByCustomerId(metaData.getCustomerId());
			return getCommunicationPrefResult(communicationEvent, cust, contactTypes);
		}
		return getCommunicationPrefResult(communicationEvent, null, contactTypes);
	}

	@Override
	public boolean sendMessage(CommunicationEvents communicationEvent, Customer commCustomer, Email email) {
		CommunicationPrefsResult communicationFlowPrefs = communicationPrefsUtil.forCustomer(communicationEvent,
				commCustomer);
		if (communicationFlowPrefs.isEmail()) {
			postManService.sendEmailAsync(email);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param communicationEvent
	 * @param model
	 * @param templateMX
	 * @param contactTypes       - channels to try for notifications, if null/empty
	 *                           attempt will be made on all channels
	 * @param customerId
	 * @return - if notification has been sent to specified contact or not
	 */

	@Override
	public boolean sendMessage(CommunicationEvents communicationEvent, Customer commCustomer, Object model,
			TemplatesMX templateMX, ContactType... contactTypes) {
		if (contactTypes.length == 0) {
			contactTypes = getContactTypes(communicationEvent);
		}

		Set<ContactType> contactTypesLists = CollectionUtil.getSet(contactTypes);
		if (contactTypesLists.contains(ContactType.SMS_EMAIL)) {
			contactTypesLists.add(ContactType.EMAIL);
			contactTypesLists.add(ContactType.SMS);
		}

		CommunicationPrefsResult communicationFlowPrefs = communicationPrefsUtil.forCustomer(communicationEvent,
				commCustomer);

		Map<String, Object> wrapper = new HashMap<String, Object>();
		wrapper.put("data", model);

		if (contactTypesLists.contains(ContactType.EMAIL) && communicationFlowPrefs.isEmail()) {
			Email email = new Email();
			email.setModel(wrapper);
			email.addTo(commCustomer.getEmail());
			email.setHtml(true);
			email.setITemplate(templateMX);
			postManService.sendEmailAsync(email);
		}

		if (contactTypesLists.contains(ContactType.WHATSAPP) && communicationFlowPrefs.isWhatsApp()) {
			WAMessage waMessage = new WAMessage();
			waMessage.setITemplate(templateMX);
			waMessage.setModel(wrapper);
			waMessage.addTo(commCustomer.getWhatsappPrefix() + commCustomer.getWhatsapp());
			whatsAppClient.send(waMessage);
		}

		if (contactTypesLists.contains(ContactType.SMS) && communicationFlowPrefs.isSms()) {
			SMS smsMessage = new SMS();
			smsMessage.setITemplate(templateMX);
			smsMessage.setModel(wrapper);
			smsMessage.addTo(commCustomer.getMobile());
			postManService.sendSMSAsync(smsMessage);
		}

		if (contactTypesLists.contains(ContactType.FBPUSH) && communicationFlowPrefs.isPushNotify()) {
			PushMessage pushMessage = new PushMessage();
			pushMessage.setITemplate(templateMX);
			pushMessage.setModel(wrapper);
			pushMessage.addToUser(commCustomer.getCustomerId());
			pushNotifyClient.send(pushMessage);
		}

		return false;
	}

}
