package com.amx.jax.customer.task;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dict.AmxEnums.CommunicationEvents;
import com.amx.jax.dict.Language;
import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.client.WhatsAppClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.jax.util.CommunicationPrefsUtil;
import com.amx.jax.util.CommunicationPrefsUtil.CommunicationPrefsResult;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@TunnelEventMapping(topic = AmxTunnelEvents.Names.BIRTHDAY_WISHES,scheme = TunnelEventXchange.TASK_WORKER)
public class BirthdayWishesListener implements ITunnelSubscriber<DBEvent> {
	@Autowired
	PostManService postManService;

	@Autowired
	private PushNotifyClient pushNotifyClient;
	
	@Autowired
	private WhatsAppClient whatsAppClient;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	CommunicationPrefsUtil communicationPrefsUtil;
	
	private static final String CUST_ID = "CUST_ID";
	private static final String LANG_ID = "LANG_ID";
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Override
	public void onMessage(String channel, DBEvent event) {
		LOGGER.debug("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));
		
		BigDecimal custId = ArgUtil.parseAsBigDecimal(event.getData().get(CUST_ID));
		BigDecimal langId = ArgUtil.parseAsBigDecimal(event.getData().get(LANG_ID));
		Customer customer = customerRepository.getCustomerByCustomerIdAndIsActive(custId, "Y");
		CommunicationPrefsResult x = communicationPrefsUtil.forCustomer(CommunicationEvents.BIRTHDAY_WISHES, customer);
		LOGGER.debug("Comm pref Util result is "+x.isWhatsApp());
		if (x.isEmail()) {
			Email email = new Email();
			if ("2".equals(langId)) {
				email.setLang(Language.AR);
				
			} else {
				email.setLang(Language.EN);
				
			}
			email.addTo(customer.getEmail());
			email.setITemplate(TemplatesMX.BIRTHDAY_WISH);
			
			postManService.sendEmailAsync(email);
		}

		if (x.isWhatsApp()) {
			WAMessage waMessage = new WAMessage();
			waMessage.setITemplate(TemplatesMX.BIRTHDAY_WISH);
			waMessage.addTo(customer.getWhatsappPrefix() + customer.getWhatsapp());
			whatsAppClient.send(waMessage);
		}

		if (x.isSms()) {
			SMS smsMessage = new SMS();
			smsMessage.setITemplate(TemplatesMX.BIRTHDAY_WISH);
			smsMessage.addTo(customer.getPrefixCodeMobile()+customer.getMobile());
			postManService.sendSMSAsync(smsMessage);
		}

		if (x.isPushNotify()) {
			PushMessage pushMessage = new PushMessage();
			pushMessage.setITemplate(TemplatesMX.BIRTHDAY_WISH);
			pushMessage.addToUser(custId);
			pushNotifyClient.send(pushMessage);
		}
	}
	
	

}
